plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.scan")
}

dependencies {
    api(project(":client:component-scan:external"))
    implementation(project(":client:component-scan:internal"))
}
