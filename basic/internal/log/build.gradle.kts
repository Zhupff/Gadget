plugins {
    id("gadget.android.library")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    android("gadget.basic.log")
    enableAutoService()
}

dependencies {
    implementation(project(":basic:external"))
}
