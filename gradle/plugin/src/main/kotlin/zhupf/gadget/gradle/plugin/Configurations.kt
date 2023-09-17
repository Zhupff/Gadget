package zhupf.gadget.gradle.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.text.SimpleDateFormat

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
    pluginManager.apply("org.jetbrains.kotlin.kapt")
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
        "kapt"(libs.findLibrary("androidx-lifecycle-compiler").get())
        "implementation"(libs.findLibrary("androidx-lifecycle-livedata-ktx").get())
        "implementation"(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
        "implementation"(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
        "implementation"(libs.findLibrary("androidx-lifecycle-viewModel-compose").get())
        "implementation"(libs.findLibrary("androidx-lifecycle-viewModel-ktx").get())
        "implementation"(libs.findLibrary("androidx-lifecycle-viewModel-savedstate").get())
    }
}

fun Project.configurePublish() {
    pluginManager.apply("maven-publish")
    afterEvaluate {
        extensions.configure(PublishingExtension::class.java) {
            repositories {
                mavenLocal()
            }
            publications {
                create("MavenLocalPublication", MavenPublication::class.java) {
                    from(components.getByName(
                        if (this@configurePublish.pluginManager.hasPlugin("com.android.library"))
                            "release"
                        else
                            "java"
                    ))
                    artifactId = this@configurePublish.name
                    version = GADGET_VERSION
                }
            }
        }
    }
}

val GADGET_VERSION = SimpleDateFormat("YY.MM.dd").format(System.currentTimeMillis())