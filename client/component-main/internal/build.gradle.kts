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
    implementation(project(":client:basic-android"))
    implementation(project(":client:component-main:external"))
}
