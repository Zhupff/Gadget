package gadget

import com.google.gson.Gson
import gadget.basic.udp.UdpDiscover
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ServerReady
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.io.File

fun main() {

    embeddedServer(Netty, port = Alyx.getHttpPort(), host = "0.0.0.0") {

        monitor.subscribe(ServerReady) {
            UdpDiscover.start()
        }

        monitor.subscribe(ApplicationStopped) {
            UdpDiscover.stop()
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
