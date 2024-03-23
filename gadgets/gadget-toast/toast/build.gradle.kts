plugins {
    id("gadget.library")
}

script {
    configuration("zhupf.gadgets.toast") {
        configure()
    }
    publication {
        publish()
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.startup)
}