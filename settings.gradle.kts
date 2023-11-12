pluginManagement {
    includeBuild("gradle")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Gadget"
include(":app")

include(":component-homepage")
include(":component-homepage:feature")

include(":module")

include(":gadgets:common")
include(":gadgets:common-android")
include(":gadgets:logger")
include(":gadgets:widget")
include(":gadgets:widget-compile")