plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.main")
}

dependencies {
    implementation(project(":component-main:external"))
    implementation(project(":component-main:internal"))
}
