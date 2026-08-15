package gadget.basic.network

import com.google.auto.service.AutoService
import gadget.IApp
import gadget.basic.kv.DataStoreProvider
import gadget.basic.kv.ProtoSerializer
import gadget.basic.link.GLink
import gadget.basic.logger.Logger
import gadget.basic.tool.Ciphering
import gadget.basic.tool.nextString
import gadget.basic.tool.singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.SocketTimeoutException
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.Base64
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object LocalServer {

    var host: String? = null
        private set

    var client: OkHttpClient? = null
        private set

    var retrofit: Retrofit? = null
        private set

    interface ILocalServerConfigDataStoreProvider : DataStoreProvider<LocalServerConfigProto>

    @AutoService(IApp.Startup.MainStartup::class)
    internal class InitTask : IApp.Startup.MainStartup {

        override val priority: Int = 1

        override suspend fun post() {
            withContext(Dispatchers.IO) {
                val localServerConfig = singleton<ILocalServerConfigDataStoreProvider>()
                    .provide().data.first {
                        it.id.isNotBlank()
                    }
                DatagramSocket(null).use { socket ->
                    socket.reuseAddress = true
                    socket.broadcast = true
                    socket.bind(InetSocketAddress("0.0.0.0", 0))
                    socket.soTimeout = 100

                    val udpRequest = UdpDiscoverRequestProto(
                        clientId = UUID.randomUUID().toString(),
                        clientSecret = SecureRandom().nextString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", 16),
                    )
                    val requestBytes = Ciphering.AES256.encrypt(localServerConfig.secret, UdpDiscoverRequestProto.ADAPTER.encode(udpRequest))

                    var retry = 0
                    while (retry < 3) {
                        currentCoroutineContext().ensureActive()
                        if (retry++ > 0) {
                            delay(100L)
                        }
                        buildSet {
                            add(InetAddress.getByName("255.255.255.255"))
                            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
                            while (networkInterfaces.hasMoreElements()) {
                                val networkInterface = networkInterfaces.nextElement()
                                if (!networkInterface.isUp || networkInterface.isLoopback || networkInterface.isVirtual) {
                                    continue
                                }
                                networkInterface.interfaceAddresses.forEach { address ->
                                    address.broadcast?.let(::add)
                                }
                            }
                        }.forEach { address ->
                            runCatching {
                                socket.send(DatagramPacket(requestBytes, requestBytes.size, address, localServerConfig.udpPort))
                            }
                        }

                        val packet = DatagramPacket(
                            ByteArray(UdpDiscoverContract.MAX_PACKET_SIZE),
                            UdpDiscoverContract.MAX_PACKET_SIZE,
                        )
                        try {
                            socket.receive(packet)
                        } catch (_: SocketTimeoutException) {
                            continue
                        }
                        if (packet.address !is Inet4Address) {
                            continue
                        }
                        val encrypted = packet.data.copyOfRange(packet.offset, packet.offset + packet.length)
                        val decrypted = Ciphering.AES256.decrypt(udpRequest.clientSecret, encrypted)
                        val udpResponse = UdpDiscoverResponseProto.ADAPTER.decode(decrypted)
                        if (udpResponse.clientId != udpRequest.clientId || udpResponse.serverId != localServerConfig.id) {
                            continue
                        }
                        doInit(
                            localServerConfig.host,
                            localServerConfig.httpPort,
                            packet.address.hostAddress,
                            udpResponse.certificate,
                        )
                        break
                    }
                }
            }
        }

        private fun doInit(host: String, port: Int, ip: String, cert: String) {
            val trustManager = object : X509TrustManager {
                override fun checkServerTrusted(chain: Array<out X509Certificate>, authType: String) {
                    if (chain.isEmpty()) {
                        throw CertificateException("Empty server certificate chain")
                    }
                    val decodedCert = Base64.getEncoder().encodeToString(
                        MessageDigest.getInstance("SHA-256").digest(chain[0].publicKey.encoded)
                    )
                    if (decodedCert != cert) {
                        throw CertificateException("Server certificate public key pin mismatch")
                    }
                }
                override fun checkClientTrusted(chain: Array<out X509Certificate>, authType: String) = Unit
                override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
            }
            val sslContext = SSLContext.getInstance("TLS").also {
                it.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
            }
            LocalServer.client = OkHttpClient.Builder()
                .connectTimeout(16L, TimeUnit.SECONDS)
                .readTimeout(32L, TimeUnit.SECONDS)
                .writeTimeout(32L, TimeUnit.SECONDS)
                .sslSocketFactory(sslContext.socketFactory, trustManager)
                .dns {
                    if (it == host) {
                        listOf(InetAddress.getByName(ip))
                    } else {
                        okhttp3.Dns.SYSTEM.lookup(it)
                    }
                }
                .build()
            LocalServer.retrofit = Retrofit.Builder()
                .client(LocalServer.client)
                .baseUrl("https://${host}:${port}")
                .build()
            LocalServer.host = host
        }
    }

    @AutoService(gadget.basic.link.GLinkHandler::class)
    internal class GLinkHandler : gadget.basic.link.GLinkHandler {
        override val biz: String = "local-server"
        override fun handle(link: GLink) {
            if (link.path.firstOrNull() == "config") {
                val base64 = link.params["base64"]
                if (base64.isNullOrBlank()) {
                    return
                }
                val bytes = Base64.getUrlDecoder().decode(base64)
                val config = LocalServerConfigProto.ADAPTER.decode(bytes)
                ProtoSerializer.ioScope.launch {
                    runCatching {
                        singleton<ILocalServerConfigDataStoreProvider>()
                            .provide()
                            .updateData { config }
                    }.onFailure { throwable ->
                        Logger.w("LocalServer", throwable) {
                            "Failed to handle local server config!"
                        }
                    }
                }
            }
        }
    }
}