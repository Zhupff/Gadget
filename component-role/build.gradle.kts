plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.role")
}

dependencies {
    implementation(project(":component-role:external"))
    implementation(project(":component-role:internal"))
}
