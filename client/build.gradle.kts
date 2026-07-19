plugins {
    id("gadget.android.application")
}

gadget {
    android("alyx.gadget") {
        defaultConfig {
            versionCode = 1_000_000
            versionName = "1.0.0"
        }
    }
}

dependencies {
    implementation(project(":client:basic"))
    implementation(project(":client:basic:logger"))
    implementation(project(":client:basic:theme"))
    implementation(project(":client:basic:ui"))
}
