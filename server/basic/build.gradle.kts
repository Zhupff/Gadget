plugins {
    id("gadget.jvm")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    enableAutoService()
}

dependencies {
    api(project(":common"))
    implementation(libs.google.zxing.core)
    api(libs.ktor.server.core)
    implementation(libs.ktor.tls)
}
