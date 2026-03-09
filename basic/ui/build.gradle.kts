plugins {
    id("gadget.android.library")
    alias(libs.plugins.kotlin.ksp)
}

gadget {
    android("gadget.basic.ui")
}

dependencies {
    api(project(":basic:annotation"))
    ksp(project(":basic:compile"))
    api(libs.androidx.recyclerview)
}
