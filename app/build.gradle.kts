plugins {
    id("gadget.android.application")
}

gadget {
    android("gadget") {
        defaultConfig {
            versionCode = 1_000_000
            versionName = "1.0.0"
        }
    }
    enableJunitTest()
    enableViewBinding()
}

dependencies {
    implementation(project(":basic:basic-log"))
    implementation(project(":component-main:internal"))
}
