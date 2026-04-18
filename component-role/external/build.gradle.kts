plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.role.external")
    enableProtobuf()
}

dependencies {
    implementation(project(":basic"))
}
