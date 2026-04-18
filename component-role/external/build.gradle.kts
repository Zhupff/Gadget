plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.role.external")
}

dependencies {
    implementation(project(":basic"))
}
