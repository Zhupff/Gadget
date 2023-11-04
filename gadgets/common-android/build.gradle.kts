plugins {
    id("zhupf.gadget.library")
}

gadget {
    ANDROIDPUBLICATION("zhupf.gadget.common")
}

dependencies {
    implementation(libs.androidx.startup)
    compileOnly(libs.androidx.lifecycle.livedata.ktx)
}