import java.util.Properties

plugins {
    id("gadget.jvm")
    alias(libs.plugins.ktor)
}

application {
    mainClass = "gadget.MainKt"
}

dependencies {
    implementation(libs.ktor.server.netty)

    implementation(project(":server:basic"))
    implementation(project(":server:basic:logger"))
    implementation(project(":server:basic:udp"))
}

Properties().let { localProperties ->
    rootProject.file("local.properties")
        .inputStream()
        .use(localProperties::load)
    val udpPort = localProperties.getProperty("alyx.udp.port")
        ?: error("Missing alyx.udp.port in local.properties")
    val httpPort = localProperties.getProperty("alyx.http.port")
        ?: error("Missing alyx.http.port in local.properties")
    val serverSecret = localProperties.getProperty("alyx.server.secret")
        ?: error("Missing alyx.server.secret in local.properties")

    tasks.withType<JavaExec>().configureEach {
        systemProperty("alyx.udp.port", udpPort)
        systemProperty("alyx.http.port", httpPort)
        systemProperty("alyx.server.secret", serverSecret)
    }
}
