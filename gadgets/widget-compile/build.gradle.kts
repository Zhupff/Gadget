plugins {
    id("zhupf.gadget.jvm")
}

gadget {
    JVMPUBLICATION()
}

dependencies {
    implementation(libs.kotlin.ksp.api)
    implementation(libs.squareup.kotlinpoet)
}