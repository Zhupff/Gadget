import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

abstract class GadgetAndroidPlugin<E : CommonExtension<*, *, *, *, *, *>> : GadgetPlugin() {

    protected abstract val androidExtension: E

    open fun android(ns: String) {
        androidExtension.apply {
            namespace = ns
            compileSdk = 36
            defaultConfig.minSdk = 32
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
        project.tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
                freeCompilerArgs.addAll(
                    "-module-name",
                    this@GadgetAndroidPlugin.project.path.replaceFirst(":", "").replace(":", "-"),
                )
            }
        }
    }

    open fun android(ns: String, closure: E.() -> Unit) {
        android(ns)
        closure(androidExtension)
    }
}
