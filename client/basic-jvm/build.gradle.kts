plugins {
    id("gadget.jvm")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    enableAutoService()
    enableProtobuf()
}

dependencies {
    api(project(":common"))
    api(libs.squareup.okhttp3)
    api(libs.squareup.retrofit2)
    compileOnly(libs.androidx.datastore.core)
}
