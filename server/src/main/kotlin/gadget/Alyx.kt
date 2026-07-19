package gadget

import com.google.gson.Gson
import gadget.basic.logger.Logger
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.io.File
import java.net.Inet4Address
import java.net.NetworkInterface

fun main() {
    embeddedServer(Netty, port = 3721, host = "0.0.0.0") {
        Logger.i("Alyx") {
            "Server(${getAvailableLocalIPs()}) started!"
        }
        val videoDir = File("")
        routing {
            get("/") {
                call.respondText("GADGET")
            }
            get("/videos") {
                val files = videoDir.list().toList().shuffled()
                call.respondText(Gson().toJson(files))
            }
            get("/video/{file}") {
                val file = call.parameters["file"]!!
                call.respondFile(videoDir.resolve(file))
            }
        }
    }.start(wait = true)
}

fun getAvailableLocalIPs(): List<String> {
    val ipList = mutableListOf<String>()
    try {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val networkInterface = interfaces.nextElement()
            // 过滤掉未启用的网卡、虚拟网卡以及回环网卡(Loopback)
            if (!networkInterface.isUp || networkInterface.isLoopback || networkInterface.isVirtual) {
                continue
            }
            val addresses = networkInterface.inetAddresses
            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()
                // 只保留 IPv4 地址（过滤掉 IPv6）
                if (address is Inet4Address) {
                    ipList.add(address.hostAddress)
                }
            }
        }
    } catch (e: Exception) {
        // 防止由于权限或系统限制抛出异常
    }
    return ipList
}