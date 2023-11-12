import org.gradle.api.JavaVersion
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.dependencies

abstract class Configuration internal constructor(val gadget: Gadget) {

    abstract fun configure()

    class Empty internal constructor(gadget: Gadget) : Configuration(gadget) {
        override fun configure() {}
    }

    abstract class Android internal constructor(gadget: Gadget, val namespace: String) : Configuration(gadget) {
        override fun configure() {
            with(gadget.project) {
                pluginManager.apply(KOTLIN_ANDROID_ID)
                androidExtension.apply {
                    namespace = this@Android.namespace
                    compileSdk = 33
                    defaultConfig {
                        minSdk = 24
                        compileOptions {
                            sourceCompatibility = JavaVersion.VERSION_17
                            targetCompatibility = JavaVersion.VERSION_17
                        }
                        kotlinOptions {
                            jvmTarget = JavaVersion.VERSION_17.toString()
                            freeCompilerArgs = freeCompilerArgs + listOf(
                                "-module-name",
                                project.path.replaceFirst(":", "").replace(":", "-")
                            )
                        }
                        packaging {
                            resources {
                                excludes += "/META-INF/{AL2.0,LGPL2.1}"
                            }
                        }
                    }
                    dependencies {
                        "implementation"(libs.findLibrary("androidx-core-ktx").get())
                        "implementation"(libs.findLibrary("androidx-appcompat").get())
//                        "implementation"(libs.findLibrary("androidx-lifecycle-livedata-ktx").get())
//                        "implementation"(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
//                        "implementation"(libs.findLibrary("androidx-lifecycle-viewModel-ktx").get())
                    }
                }
            }
        }

        class Application internal constructor(gadget: Gadget, namespace: String) : Android(gadget, namespace) {
            override fun configure() {
                assert(gadget.project.pluginManager.hasPlugin(ANDROID_APPLICATION_ID))
                super.configure()
                with(gadget.project) {
                    applicationExtension.apply {
                        defaultConfig {
                            applicationId = "zhupf.gadget"
                            versionName = GADGET_VERSION
                            println("applicationId=$applicationId, versionName=$versionName")
                        }
                    }
                }
            }
        }

        open class Library internal constructor(gadget: Gadget, namespace: String) : Android(gadget, namespace) {
            override fun configure() {
                assert(gadget.project.pluginManager.hasPlugin(ANDROID_LIBRARY_ID))
                super.configure()
            }

            class Publication internal constructor(gadget: Gadget, namespace: String) : Library(gadget, namespace) {
                override fun configure() {
                    super.configure()
                    with(gadget.project) {
                        pluginManager.apply(MAVEN_PUBLISH_ID)
                        afterEvaluate {
                            extensions.configure(PublishingExtension::class.java) {
                                repositories {
                                    mavenLocal()
                                }
                                publications {
                                    create("MavenLocalPublication", MavenPublication::class.java) {
                                        from(components.getByName("release"))
                                        artifactId = gadget.project.name
                                        version = GADGET_VERSION
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    open class Jvm internal constructor(gadget: Gadget) : Configuration(gadget) {
        override fun configure() {}

        class Publication(gadget: Gadget) : Jvm(gadget) {
            override fun configure() {
                super.configure()
                with(gadget.project) {
                    pluginManager.apply(MAVEN_PUBLISH_ID)
                    afterEvaluate {
                        extensions.configure(PublishingExtension::class.java) {
                            repositories {
                                mavenLocal()
                            }
                            publications {
                                create("MavenLocalPublication", MavenPublication::class.java) {
                                    from(components.getByName("java"))
                                    artifactId = gadget.project.name
                                    version = GADGET_VERSION
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun <T : Gadget> T.APPLICATION(namespace: String, closure: @GradleScope Configuration.Android.Application.() -> Unit = {}) =
    Configuration.Android.Application(this, namespace).configure(closure)
fun <T : Gadget> T.LIBRARY(namespace: String, closure: @GradleScope Configuration.Android.Library.() -> Unit = {}) =
    Configuration.Android.Library(this, namespace).configure(closure)
fun <T : Gadget> T.ANDROIDPUBLICATION(namespace: String, closure: @GradleScope Configuration.Android.Library.Publication.() -> Unit = {}) =
    Configuration.Android.Library.Publication(this, namespace).configure(closure)
fun <T : Gadget> T.JVM(closure: @GradleScope Configuration.Jvm.() -> Unit = {}) =
    Configuration.Jvm(this).configure(closure)
fun <T : Gadget> T.JVMPUBLICATION(closure: @GradleScope Configuration.Jvm.Publication.() -> Unit = {}) =
    Configuration.Jvm.Publication(this).configure(closure)
private fun <T : Configuration> T.configure(closure: @GradleScope T.() -> Unit) {
    gadget.configuration = this
    closure(this)
}