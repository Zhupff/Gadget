plugins {
    id("zhupf.gadget.library")
}

gadget {
    ANDROIDPUBLICATION("zhupf.gadget.theme")
}

dependencies {
    compileOnly(libs.androidx.core.ktx)
    compileOnly(libs.androidx.appcompat)
}