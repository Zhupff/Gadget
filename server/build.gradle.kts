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
}

Properties().let { localProperties ->
    rootProject.file("local.properties")
        .inputStream()
        .use(localProperties::load)
    val serverProperties = localProperties.stringPropertyNames()
        .filter { it.startsWith("[server]") }
        .associateWith { localProperties.getProperty(it) }
    tasks.withType<JavaExec>().configureEach {
        systemProperties(serverProperties)
    }
}
