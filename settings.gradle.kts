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
    ":client:basic-jvm",
    ":client:basic-android",
    ":client:component-main",
    ":client:component-main:external",
    ":client:component-main:internal",
    ":client:component-scan",
    ":client:component-scan:external",
    ":client:component-scan:internal",
)

include(
    ":server",
    ":server:basic",
)

include(
    ":common",
)
