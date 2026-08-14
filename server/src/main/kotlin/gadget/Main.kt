package gadget

import gadget.basic.config.ServerConfiguration
import gadget.basic.network.TLS
import gadget.basic.network.UdpDiscover
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ServerReady
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main() {

    embeddedServer(
        factory = Netty,
        configure = {
            TLS.sslConnect(this) {
                host = "0.0.0.0"
                port = ServerConfiguration.httpPort
                enabledProtocols = listOf("TLSv1.3", "TLSv1.2")
            }
        },
        module = {

            monitor.subscribe(ServerReady) {
                ServerConfiguration.toQRCode()
                UdpDiscover.start()
            }

            monitor.subscribe(ApplicationStopped) {
                UdpDiscover.stop()
            }

            routing {
                get("/") {
                    call.respondText("GADGET")
                }
            }
        }
    ).start(wait = true)
}
