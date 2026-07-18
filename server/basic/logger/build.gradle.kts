plugins {
    id("gadget.jvm")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    enableAutoService()
}

dependencies {
    implementation(project(":common"))
    implementation(project(":server:basic"))
}
