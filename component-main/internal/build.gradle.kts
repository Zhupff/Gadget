plugins {
    id("gadget.android.library")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    android("gadget.component.main.internal")
    enableAutoService()
    enableViewBinding()
}

dependencies {
    implementation(project(":basic"))
    implementation(project(":basic:theme"))
    implementation(project(":component-main:external"))
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.window)
}
