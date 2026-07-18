pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("gradle")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Gadget"

include(
    ":client",
    ":client:basic",
    ":client:basic:annotation",
    ":client:basic:logger",
    ":client:basic:ui",
)

include(
    ":server",
    ":server:basic",
    ":server:basic:logger",
)

include(
    ":common",
)
