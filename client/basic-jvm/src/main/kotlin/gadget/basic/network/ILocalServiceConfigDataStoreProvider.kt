package gadget.basic.network

import com.google.auto.service.AutoService
import gadget.IApp
import gadget.basic.kv.DataStoreProvider
import gadget.basic.tool.GSON
import gadget.basic.tool.nextString
import gadget.basic.tool.singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.SocketTimeoutException
import java.security.SecureRandom
import java.util.UUID

interface ILocalServiceConfigDataStoreProvider : DataStoreProvider<LocalServerConfig> {

    companion object {
        internal lateinit var serverId: String
            private set
        internal lateinit var serverCert: String
            private set
        internal lateinit var serverHost: String
            private set
        internal lateinit var serverIp: String
            private set
        internal var serverPort: Int = 0
            private set
    }

    @AutoService(IApp.Task::class)
    class InitTask : IApp.Task {

        override val priority: Int = 1

        override suspend fun execute() {
            withContext(Dispatchers.IO) {
                val localServerConfig = singleton<ILocalServiceConfigDataStoreProvider>()
                    .provide().data.first {
                        it.id.isNotBlank()
                    }
                serverId = localServerConfig.id
                serverHost = localServerConfig.host
                serverPort = localServerConfig.httpPort
                DatagramSocket(null).use { socket ->
                    socket.reuseAddress = true
                    socket.broadcast = true
                    socket.bind(InetSocketAddress("0.0.0.0", 0))
                    socket.soTimeout = 100

                    val udpDiscoverRequest = UdpDiscoverRequest(
                        clientId = UUID.randomUUID().toString(),
                        secret = SecureRandom().nextString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789", 10),
                    )
                    val requestBytes = UdpDiscoverProtocol.encrypt(localServerConfig.secret, GSON.toJson(udpDiscoverRequest)).toByteArray(Charsets.UTF_8)

                    while (true) {
                        currentCoroutineContext().ensureActive()
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
                            ByteArray(UdpDiscoverProtocol.MAX_PACKET_SIZE),
                            UdpDiscoverProtocol.MAX_PACKET_SIZE,
                        )
                        try {
                            socket.receive(packet)
                        } catch (_: SocketTimeoutException) {
                            continue
                        }
                        if (packet.address !is Inet4Address) {
                            continue
                        }
                        val encrypted = String(packet.data, packet.offset, packet.length, Charsets.UTF_8)
                        val decrypted = UdpDiscoverProtocol.decrypt(udpDiscoverRequest.secret, encrypted)
                        val udpDiscoverResponse = GSON.fromJson(decrypted, UdpDiscoverResponse::class.java)
                        if (udpDiscoverResponse.clientId != udpDiscoverRequest.clientId) {
                            continue
                        }
                        serverCert = udpDiscoverResponse.certificate
                        serverIp = packet.address.hostAddress
                        break
                    }
                }
            }
        }
    }
}