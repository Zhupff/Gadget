plugins {
    id("gadget.library")
}

script {
    configuration("zhupf.gadgets.basic") {
        configure()
    }
    publication {
        publish()
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.startup)
}