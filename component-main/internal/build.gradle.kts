plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.main.internal")
    enableViewBinding()
}

dependencies {
    implementation(project(":basic"))
    implementation(project(":component-main:external"))
    implementation(project(":basic:basic-theme"))
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.window)
}
