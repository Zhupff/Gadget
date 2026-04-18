plugins {
    id("gadget.android.library")
    alias(libs.plugins.kotlin.kapt)
}

gadget {
    android("gadget.component.main.internal")
    enableAutoService()
}

dependencies {
    implementation(project(":basic"))
    implementation(project(":basic:annotation"))
    implementation(project(":basic:theme"))
    implementation(project(":basic:ui"))
    implementation(project(":component-main:external"))
    "apexImplementation"(project(":component-role:external"))
}
