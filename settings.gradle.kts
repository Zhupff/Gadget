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
    ":app:server",
)

include(
    ":basic",
    ":basic:logger:client",
    ":basic:logger:server",
)
