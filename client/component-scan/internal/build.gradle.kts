plugins {
    id("gadget.android.library")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    android("gadget.component.scan.internal")
    enableAutoService()
}

dependencies {
    implementation(project(":client:basic-android"))
    implementation(project(":client:component-scan:external"))
}
