plugins {
    id("zhupf.gadget.library")
}

gadget {
    ANDROIDPUBLICATION("zhupf.gadget.logger")
}

dependencies {
    compileOnly(libs.androidx.core.ktx)
}