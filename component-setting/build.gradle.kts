plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.setting")
}

dependencies {
    implementation(project(":component-setting:external"))
    implementation(project(":component-setting:internal"))
}
