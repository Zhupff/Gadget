plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.basic.ui")
}

dependencies {
    api(libs.android.material)
    api(libs.androidx.media3.ui)
    implementation(project(":client:basic"))
    implementation(project(":client:basic:annotation"))
}
