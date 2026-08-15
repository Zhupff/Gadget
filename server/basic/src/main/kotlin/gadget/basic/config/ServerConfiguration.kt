package gadget.basic.config

import gadget.basic.link.GLink
import gadget.basic.network.LocalServerConfigProto
import gadget.basic.qrcode.QRCoder
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
        val serverConfig = LocalServerConfigProto(
            id = id,
            secret = secret,
            host = "gadget",
            udpPort = udpPort,
            httpPort = httpPort,
        )
        val base64 = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(LocalServerConfigProto.ADAPTER.encode(serverConfig))
        val glink = GLink(
            biz = "local-server",
            path = listOf("config"),
            params = mapOf("base64" to base64),
        )
        val output = Paths.get("build", "server-config.png")
        Files.createDirectories(output.parent)
        Files.write(output, QRCoder.toQRCode(glink.toString()))
    }
}