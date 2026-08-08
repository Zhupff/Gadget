package gadget.basic.config

import java.util.UUID

interface ServerConfiguration {
    val id: String
    val secret: String
    val udpPort: Int
    val httpPort: Int

    companion object : ServerConfiguration {
        override val id: String = UUID.randomUUID().toString()
        override val secret: String = System.getProperty("[server]secret")
        override val udpPort: Int = System.getProperty("[server]udp.port").toInt()
        override val httpPort: Int = System.getProperty("[server]http.port").toInt()
    }
}