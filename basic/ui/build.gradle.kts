plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.basic.ui")
}

dependencies {
    implementation(project(":basic"))
    api(libs.android.material)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.recyclerview)
}
