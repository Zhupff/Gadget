package gadget.basic.udp

import com.google.auto.service.AutoService
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

@AutoService(UdpDiscover::class)
class UdpDiscoverImpl : UdpDiscover {

    companion object {
        const val MAX_PACKET_SIZE = 1024
    }

    @Volatile
    private var discovering: Boolean = false

    private var socket: DatagramSocket? = null

    private var job: Job? = null

    private val gson = Gson()

    override fun start() {
        synchronized(this) {
            if (discovering) {
                return
            }
            discovering = true
            socket = DatagramSocket(null).apply {
                reuseAddress = true
                broadcast = true
                bind(InetSocketAddress("0.0.0.0", Alyx.getUdpPort()))
                soTimeout = 5_000
            }
            job = GlobalScope.launch(Dispatchers.IO) {
                val socket = socket!!
                while (discovering) {
                    try {
                        val buffer = ByteArray(MAX_PACKET_SIZE)
                        val packet = DatagramPacket(buffer, buffer.size)
                        socket.receive(packet)
                        val json = String(packet.data, packet.offset, packet.length, Charsets.UTF_8)
                        Logger.i("UdpDiscover") {
                            "json=$json"
                        }
                    } catch (_: SocketTimeoutException) {
                        // ignore
                    } catch (throwable: Throwable) {
                        Logger.w("UdpDiscover", throwable) {
                            ""
                        }
                    }
                }
            }
        }
    }

    override fun stop() {
        synchronized(this) {
            if (!discovering) {
                return
            }
            discovering = false
            socket?.close()
            socket = null
            job?.cancel()
        }
    }
}