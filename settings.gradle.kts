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
include(
    ":app:basic",
)

include(
    ":gadgets",
    ":gadgets:api",
    ":gadgets:compile",
)
include(
    ":gadgets:gadget-basic",
    ":gadgets:gadget-basic:basicAndroid",
    ":gadgets:gadget-basic:basicJvm",
)
include(
    ":gadgets:gadget-logger",
    ":gadgets:gadget-logger:logger",
)
include(
    ":gadgets:gadget-toast",
    ":gadgets:gadget-toast:toast",
)