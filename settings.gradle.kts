pluginManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://jitpack.io")
    }
    includeBuild("gradle")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "Gadget"

include(":app")

include(
    ":basic",
    ":basic:annotation",
    ":basic:logger",
    ":basic:theme",
    ":basic:ui",
)

include(
    ":component-main:external",
    ":component-main:internal",
)
 