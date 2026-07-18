import gadget.basic.logger.Logger
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main() {
    Logger.i("Alyx") {
        "Server started!"
    }
    embeddedServer(Netty, port = 3721, host = "0.0.0.0") {
        routing {
            get("/") {
                call.respondText("GADGET")
            }
        }
    }.start(wait = true)
}
