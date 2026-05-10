plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.proxy")
}

dependencies {
    implementation(project(":component-proxy:external"))
    implementation(project(":component-proxy:internal"))
}
