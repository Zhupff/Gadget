package zhupf.gadget.gradle.plugin

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

open class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.android")
            androidExtension.apply {
                compileSdk = 33
                defaultConfig {
                    minSdk = 24
                    compileOptions {
                        sourceCompatibility = JavaVersion.VERSION_17
                        targetCompatibility = JavaVersion.VERSION_17
                    }
                    kotlinOptions {
                        jvmTarget = JavaVersion.VERSION_17.toString()
                        freeCompilerArgs = freeCompilerArgs + listOf("-module-name", target.path.replaceFirst(":", "").replace(":", "-"))
                    }
                    packaging {
                        resources {
                            excludes += "/META-INF/{AL2.0,LGPL2.1}"
                        }
                    }
                }
            }
            dependencies {
                "implementation"(libs.findLibrary("androidx-core-ktx").get())
            }
        }
    }
}