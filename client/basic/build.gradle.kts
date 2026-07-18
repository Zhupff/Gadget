plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.basic")
}

dependencies {
    api(project(":common"))
    api(libs.androidx.appcompat)
    api(libs.androidx.core.ktx)
}
