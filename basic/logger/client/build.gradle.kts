plugins {
    id("gadget.android.library")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    android("gadget.basic.logger")
    enableAutoService()
}

dependencies {
    implementation(project(":basic"))
}
