plugins {
    id("zhupf.gadget.android.repo")
}

android {
    namespace = "zhupf.gadget.common"
}

dependencies {
    implementation(libs.androidx.startup)
    compileOnly(libs.androidx.lifecycle.livedata.ktx)
}