plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.basic")
    enableProtobuf()
}

dependencies {
    api(libs.androidx.appcompat)
    api(libs.androidx.core.ktx)
    api(libs.androidx.datastore.protobuf)
}
