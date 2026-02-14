plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.main.internal")
}

dependencies {
    implementation(project(":basic"))
    implementation(project(":component-main:external"))
}
