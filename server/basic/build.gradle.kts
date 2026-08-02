plugins {
    id("gadget.jvm")
}

dependencies {
    api(project(":common"))
    api(libs.google.gson)
    api(libs.ktor.server.core)
}
