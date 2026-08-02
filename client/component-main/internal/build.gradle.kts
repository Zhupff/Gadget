plugins {
    id("gadget.android.library")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    android("gadget.component.main.internal")
    enableAutoService()
}

dependencies {
    implementation(libs.androidx.media3.exoplayer)
    implementation(project(":client:basic"))
    implementation(project(":client:basic:annotation"))
    implementation(project(":client:basic:network"))
    implementation(project(":client:basic:theme"))
    implementation(project(":client:basic:ui"))
    implementation(project(":client:component-main:external"))
}
