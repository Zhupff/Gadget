plugins {
    id("gadget.android.library")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    android("gadget.basic.notice")
    enableAutoService()
}

dependencies {
    implementation(project(":basic"))
}
