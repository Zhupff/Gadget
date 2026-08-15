package gadget.basic.network

import gadget.basic.config.ServerConfiguration
import gadget.basic.logger.Logger
import gadget.basic.tool.Ciphering
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

    fun start() {
        synchronized(this) {
            if (discovering != null) {
                return
            }
            socket = DatagramSocket(null).apply {
                reuseAddress = true
                broadcast = true
                bind(InetSocketAddress("0.0.0.0", ServerConfiguration.udpPort))
                soTimeout = 5_000
            }
            discovering = GlobalScope.launch(Dispatchers.IO) {
                val socket = socket!!
                while (discovering?.isActive == true) {
                    try {
                        val buffer = ByteArray(UdpDiscoverContract.MAX_PACKET_SIZE)
                        val packet = DatagramPacket(buffer, buffer.size)
                        socket.receive(packet)
                        val encrypted = packet.data.copyOfRange(packet.offset, packet.offset + packet.length)
                        val decrypted = Ciphering.AES256.decrypt(ServerConfiguration.secret, encrypted)
                        val udpRequest = UdpDiscoverRequestProto.ADAPTER.decode(decrypted)
                        val udpResponse = UdpDiscoverResponseProto(
                            serverId = ServerConfiguration.id,
                            clientId = udpRequest.clientId,
                            certificate = TLS.certificate,
                        )
                        val responseBytes = Ciphering.AES256.encrypt(udpRequest.clientSecret, UdpDiscoverResponseProto.ADAPTER.encode(udpResponse))
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