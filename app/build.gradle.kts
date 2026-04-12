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
            getByName("plus") {
                res.srcDirs(
                    "src/plus/res-language",
                )
            }
        }
    }
    enableJunitTest()
    enableViewBinding()
}

dependencies {
    implementation(project(":basic:logger"))
    implementation(project(":component-main:internal"))
}
