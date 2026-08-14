plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.component.scan.external")
}

dependencies {
    implementation(project(":client:basic-android"))
}
