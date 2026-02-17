plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.basic")
}

dependencies {
    api(libs.androidx.appcompat)
    api(libs.androidx.core.ktx)
}
