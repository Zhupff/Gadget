plugins {
    id("gadget.jvm")
}

gadget {
    enableProtobuf()
}

dependencies {
    api(libs.google.gson)
    api(libs.kotlinx.coroutines.core)
    compileOnly(libs.androidx.datastore.core)
}
