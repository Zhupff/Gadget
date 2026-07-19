plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.main")
}

dependencies {
    implementation(project(":client:component-main:external"))
    implementation(project(":client:component-main:internal"))
}
