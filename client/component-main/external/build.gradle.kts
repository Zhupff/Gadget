plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.main.external")
}

dependencies {
    implementation(project(":client:basic-android"))
}
