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
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.google.zxing.core)
}
