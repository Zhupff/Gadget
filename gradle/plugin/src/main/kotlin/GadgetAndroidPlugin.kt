import com.android.build.api.dsl.CommonExtension
import com.squareup.wire.gradle.WireExtension
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

abstract class GadgetAndroidPlugin<E : CommonExtension<*, *, *, *, *, *>> : GadgetPlugin() {

    protected open val dimensions2flavors: Map<String, List<String>> = mapOf(
        "tier" to listOf("lite", "plus"),
    )

    protected abstract val androidExtension: E

    open fun android(namespace: String) {
        androidExtension.namespace = namespace
        androidExtension.apply {
            compileSdk = 35
            defaultConfig.minSdk = 32
            dimensions2flavors.forEach { (d, fs) ->
                flavorDimensions.add(d)
                productFlavors {
                    fs.forEach { f ->
                        create(f) {
                            dimension = d
                        }
                    }
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            sourceSets {
                getByName("main") {
                    java.srcDir("src/main/kotlin")
                }
                getByName("debug") {
                    java.srcDir("src/debug/kotlin")
                }
                getByName("release") {
                    java.srcDir("src/release/kotlin")
                }
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
        }
        this.project.tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
                freeCompilerArgs.addAll(
                    "-module-name",
                    this@GadgetAndroidPlugin.project.path.replaceFirst(":", "").replace(":", "-"),
                )
            }
        }
    }

    fun android(namespace: String, closure: E.() -> Unit) {
        android(namespace)
        androidExtension.closure()
    }

    fun enableAutoService() {
        this.project.dependencies.add("implementation", libs.findLibrary("autoservice-annotation").get())
        this.project.dependencies.add("kapt", libs.findLibrary("autoservice-processor").get())
    }

    fun enableViewBinding() {
        androidExtension.viewBinding.enable = true
    }

    fun enableJunitTest() {
        androidExtension.defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        this.project.dependencies.add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
        this.project.dependencies.add("androidTestImplementation", libs.findLibrary("androidx-espresso").get())
        this.project.dependencies.add("testImplementation", libs.findLibrary("junit").get())
    }

    fun enableProtobuf() {
        this.project.pluginManager.apply(libs.findPlugin("squareup-wire").get().get().pluginId)
        this.project.extensions.getByType(WireExtension::class.java).kotlin {}
    }
}