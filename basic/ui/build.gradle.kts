plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.basic.ui")
}

dependencies {
    api(project(":basic:annotation"))
    api(libs.android.material)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.recyclerview)
}
