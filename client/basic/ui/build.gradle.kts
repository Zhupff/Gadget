plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.basic.ui")
}

dependencies {
    api(libs.android.material)
    implementation(project(":client:basic"))
    implementation(project(":client:basic:annotation"))
}
