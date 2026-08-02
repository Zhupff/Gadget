package gadget

import com.google.gson.Gson
import gadget.basic.logger.Logger
import java.util.UUID

object Alyx {

    private val config = Config(
        serverId = UUID.randomUUID().toString(),
        serverSecret = System.getProperty("alyx.server.secret"),
        udpPort = System.getProperty("alyx.udp.port").toInt(),
        httpPort = System.getProperty("alyx.http.port").toInt(),
    )

    init {
        Logger.i("Alyx") {
            Gson().toJson(config)
        }
    }

    fun getServerId(): String = config.serverId

    fun getServerSecret(): String = config.serverSecret

    fun getUdpPort(): Int = config.udpPort

    fun getHttpPort(): Int = config.httpPort

    private data class Config(
        val serverId: String,
        val serverSecret: String,
        val udpPort: Int,
        val httpPort: Int,
    )
}