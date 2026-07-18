plugins {
    id("gadget.android.library")
}

gadget {
    android("gadget.basic")
}

dependencies {
    api(project(":common"))
}
