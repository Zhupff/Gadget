package zhupf.gadget.gradle.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

typealias AndroidExtension = CommonExtension<*, *, *, *, *>

fun Project.asAndroidApplication() {
    pluginManager.apply("com.android.application")
}

fun Project.asAndroidLibrary() {
    pluginManager.apply("com.android.library")
}

val Project.applicationExtension: ApplicationExtension
    get() = extensions.getByType(ApplicationExtension::class.java)

val Project.libraryExtension: LibraryExtension
    get() = extensions.getByType(LibraryExtension::class.java)

val Project.versionCatalogExtension: VersionCatalogsExtension
    get() = extensions.getByType(VersionCatalogsExtension::class.java)

val Project.androidExtension: AndroidExtension
    get() = if (pluginManager.hasPlugin("com.android.application"))
        applicationExtension
    else if (pluginManager.hasPlugin("com.android.library"))
        libraryExtension
    else
        throw RuntimeException()

val Project.libs: VersionCatalog
    get() = versionCatalogExtension.named("libs")


fun AndroidExtension.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}

fun Project.configureCommon() {
    androidExtension.apply {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("androidxComposeCompiler").get().toString()
        }
    }
    dependencies {
        "implementation"(libs.findLibrary("androidx-appcompat").get())
        "implementation"(platform(libs.findLibrary("androidx-compose-bom").get()))
        "implementation"(libs.findLibrary("androidx-activity-compose").get())
        "implementation"(libs.findLibrary("androidx-compose-material3").get())
        "implementation"(libs.findLibrary("androidx-compose-foundation").get())
        "implementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
        "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
        "implementation"(libs.findLibrary("androidx-compose-runtime").get())
        "implementation"(libs.findLibrary("androidx-compose-runtime-livedata").get())
        "implementation"(libs.findLibrary("androidx-lifecycle-livedata-ktx").get())
        "implementation"(libs.findLibrary("androidx-lifecycle-runtimeCompose").get())
        "implementation"(libs.findLibrary("androidx-lifecycle-viewModelCompose").get())
    }
}