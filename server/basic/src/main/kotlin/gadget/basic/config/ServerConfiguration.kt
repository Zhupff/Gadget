package gadget.basic.config

import gadget.basic.network.LocalServerConfig
import gadget.basic.qrcode.QRCodeJson
import gadget.basic.qrcode.QRCoder
import gadget.basic.tool.GSON
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Base64
import java.util.UUID

object ServerConfiguration {
    val id: String = UUID.randomUUID().toString()
    val secret: String = System.getProperty("[server]secret")
    val udpPort: Int = System.getProperty("[server]udp.port").toInt()
    val httpPort: Int = System.getProperty("[server]http.port").toInt()

    fun toQRCode() {
        val serverConfig = LocalServerConfig(
            id = id,
            secret = secret,
            host = "gadget",
            udpPort = udpPort,
            httpPort = httpPort,
        )
        val content = Base64.getEncoder().encodeToString(LocalServerConfig.ADAPTER.encode(serverConfig))
        val json = GSON.toJson(QRCodeJson(QRCodeJson.HEADER_LOCAL_SERVER_CONFIG, content))
        val output = Paths.get("build", "server-config.png")
        Files.createDirectories(output.parent)
        Files.write(output, QRCoder.toQRCode(json))
    }
}