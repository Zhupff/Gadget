plugins {
    id("gadget.android.application")
}

gadget {
    android("gadget") {
        defaultConfig {
            versionCode = 1_000_000
            versionName = "1.0.0"
        }
        sourceSets {
            getByName("apex") {
                res.srcDirs(
                    "src/apex/res-language",
                )
            }
        }
    }
    enableJunitTest()
}

dependencies {
    implementation(project(":basic:logger"))
    implementation(project(":component-main:internal"))
}
