package gadget.basic.udp

import com.google.gson.Gson
import gadget.basic.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.SocketTimeoutException
import java.util.UUID

class UdpDiscover {

    @Volatile
    private var discovering: Job? = null


    fun start() {
        synchronized(this) {
            if (discovering != null) {
                return
            }
            discovering = GlobalScope.launch(Dispatchers.IO) {
                DatagramSocket(null).use { socket ->
                    socket.reuseAddress = true
                    socket.broadcast = true
                    socket.bind(InetSocketAddress("0.0.0.0", 0))
                    socket.soTimeout = 500

                    val udpDiscoverRequest = UdpDiscoverRequest(
                        clientId = UUID.randomUUID().toString(),
                        secret = "yyds",
                    )
                    val requestBytes = UdpDiscoverProtocol.encrypt("yysy", Gson().toJson(udpDiscoverRequest)).toByteArray(Charsets.UTF_8)

                    var repeat = 3
                    while (repeat > 0) {
                        currentCoroutineContext().ensureActive()
                        repeat--

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
                                socket.send(DatagramPacket(requestBytes, requestBytes.size, address, 7749))
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
                        Logger.d("@@@") {
                            "serverIp=${packet.address.hostAddress} encrypted=${encrypted} decrypted=$decrypted"
                        }
                    }
                    stop()
                }
            }
        }
    }

    fun stop() {
        synchronized(this) {
            if (discovering == null) {
                return
            }
            discovering?.cancel()
            discovering = null
        }
    }
}