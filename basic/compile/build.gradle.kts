plugins {
    id("gadget.jvm")
}

dependencies {
    implementation(project(":basic:annotation"))
    implementation(libs.kotlin.ksp.api)
    implementation(libs.squareup.kotlinpoet)
}
