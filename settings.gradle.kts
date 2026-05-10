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
    ":basic:notice",
    ":basic:theme",
    ":basic:ui",
)

include(
    ":component-main",
    ":component-main:external",
    ":component-main:internal",
)

include(
    ":component-proxy",
    ":component-proxy:external",
    ":component-proxy:internal",
)

include(
    ":component-role",
    ":component-role:external",
    ":component-role:internal",
)

include(
    ":component-setting",
    ":component-setting:external",
    ":component-setting:internal",
)
 