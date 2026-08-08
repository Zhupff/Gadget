plugins {
    id("gadget.jvm")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    enableAutoService()
}

dependencies {
    api(project(":common"))
    api(libs.ktor.server.core)
}
