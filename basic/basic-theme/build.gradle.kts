plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.basic.theme")
}

dependencies {
    implementation(project(":basic"))
}
