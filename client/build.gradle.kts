plugins {
    id("gadget.android.application")
}

gadget {
    android("alyx.gadget") {
        defaultConfig {
            versionCode = 1_000_000
            versionName = "1.0.0"
        }
        sourceSets {
            getByName("main") {
                res.srcDirs(
                    "src/main/res-language",
                )
            }
        }
    }
}

dependencies {
    implementation(project(":client:basic-android"))
    implementation(project(":client:component-main"))
    implementation(project(":client:component-scan"))
}
