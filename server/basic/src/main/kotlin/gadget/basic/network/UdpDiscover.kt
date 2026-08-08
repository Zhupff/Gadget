package gadget.basic.network

import com.google.gson.Gson
import gadget.Alyx
import gadget.basic.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.SocketTimeoutException

object UdpDiscover {

    @Volatile
    private var discovering: Job? = null

    private var socket: DatagramSocket? = null

    private val gson = Gson()

    fun start() {
        synchronized(this) {
            if (discovering != null) {
                return
            }
            socket = DatagramSocket(null).apply {
                reuseAddress = true
                broadcast = true
                bind(InetSocketAddress("0.0.0.0", Alyx.getUdpPort()))
                soTimeout = 5_000
            }
            discovering = GlobalScope.launch(Dispatchers.IO) {
                val socket = socket!!
                while (discovering?.isActive == true) {
                    try {
                        val buffer = ByteArray(UdpDiscoverProtocol.MAX_PACKET_SIZE)
                        val packet = DatagramPacket(buffer, buffer.size)
                        socket.receive(packet)
                        val encrypted = String(packet.data, packet.offset, packet.length, Charsets.UTF_8)
                        val decrypted = UdpDiscoverProtocol.decrypt(Alyx.getServerSecret(), encrypted)
                        Logger.d("UdpDiscover") {
                            "encrypted=$encrypted decrypted=$decrypted"
                        }
                        val udpDiscoverRequest = gson.fromJson(decrypted, UdpDiscoverRequest::class.java)
                        val udpDiscoverResponse = UdpDiscoverResponse(
                            clientId = udpDiscoverRequest.clientId,
                            serverId = Alyx.getServerId(),
                            httpPort = Alyx.getHttpPort(),
                        )
                        val responseBytes = UdpDiscoverProtocol.encrypt(udpDiscoverRequest.secret, gson.toJson(udpDiscoverResponse)).toByteArray(Charsets.UTF_8)
                        socket.send(DatagramPacket(responseBytes, responseBytes.size, packet.address, packet.port))
                    } catch (_: SocketTimeoutException) {
                        // ignore
                    } catch (throwable: Throwable) {
                        Logger.w("UdpDiscover", throwable) { "" }
                    }
                }
            }
        }
    }

    fun stop() {
        synchronized(this) {
            if (discovering == null) {
                return
            }
            socket?.close()
            socket = null
            discovering?.cancel()
        }
    }
}