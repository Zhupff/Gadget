plugins {
    id("gadget.android.library")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    android("gadget.component.role.internal")
    enableAutoService()
}

dependencies {
    implementation(project(":basic"))
    implementation(project(":basic:annotation"))
    implementation(project(":basic:theme"))
    implementation(project(":basic:ui"))
    implementation(project(":component-main:external"))
    implementation(project(":component-role:external"))
}
