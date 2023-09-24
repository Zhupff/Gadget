plugins {
    id("zhupf.gadget.kotlin.repo")
}

dependencies {
    implementation(libs.kotlin.ksp.api)
    implementation(libs.squareup.kotlinpoet)
}