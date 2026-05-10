plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.proxy.external")
    enableProtobuf()
}

dependencies {
    implementation(project(":basic"))
}
