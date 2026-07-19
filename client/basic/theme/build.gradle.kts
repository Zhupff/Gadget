plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.basic.theme")
}

dependencies {
    implementation(libs.android.material)
    implementation(project(":client:basic"))
}
