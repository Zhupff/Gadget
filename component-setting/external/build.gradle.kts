plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.setting.external")
    enableProtobuf()
}

dependencies {
    implementation(project(":basic"))
}
