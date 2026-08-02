package gadget.basic.udp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.SocketTimeoutException

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

                    val requestBytes = "Helo".toByteArray(Charsets.UTF_8)
                    var repeat = 3
                    while (repeat > 0) {
                        currentCoroutineContext().ensureActive()
                        repeat--
                        val packet = DatagramPacket(
                            ByteArray(UdpDiscoverProtocol.MAX_PACKET_SIZE),
                            UdpDiscoverProtocol.MAX_PACKET_SIZE,
                        )
                        try {
                            socket.receive(packet)
                        } catch (_: SocketTimeoutException) {
                            continue
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