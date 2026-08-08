import com.android.build.api.dsl.CommonExtension
import com.squareup.wire.gradle.WireExtension
import org.gradle.api.JavaVersion

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
    }

    open fun android(ns: String, closure: E.() -> Unit) {
        android(ns)
        closure(androidExtension)
    }

    fun enableProtobuf() {
        project.pluginManager.apply(libs.findPlugin("squareup-wire").get().get().pluginId)
        project.extensions.getByType(WireExtension::class.java).kotlin {}
    }
}
