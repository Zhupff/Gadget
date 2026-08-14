plugins {
    id("gadget.android.library")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    android("gadget.basic")
    enableAutoService()
}

dependencies {
    api(project(":client:basic-jvm"))
    api(libs.android.material)
    api(libs.androidx.appcompat)
    api(libs.androidx.core.ktx)
    api(libs.androidx.datastore)
    compileOnly(libs.androidx.camera.view)
    compileOnly(libs.androidx.media3.ui)
}
