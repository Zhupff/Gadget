plugins {
    id("gadget.jvm")
    alias(libs.plugins.ktor)
}

application {
    mainClass = "AlyxKt"
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)

    implementation(project(":server:basic"))
    implementation(project(":server:basic:logger"))
}
