plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.main.internal")
    enableViewBinding()
}

dependencies {
    implementation(project(":basic"))
    implementation(project(":basic:theme"))
    implementation(project(":component-main:external"))
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.window)
}
