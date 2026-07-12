import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion

abstract class GadgetAndroidPlugin<E : CommonExtension> : GadgetPlugin() {

    protected abstract val androidExtension: E

    open fun android(ns: String) {
        androidExtension.apply {
            namespace = ns
            compileSdk = 36
            defaultConfig.minSdk = 32
            compileOptions.apply {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
            sourceSets.apply {
                getByName("main").kotlin.directories += "src/main/kotlin"
                getByName("debug").kotlin.directories += "src/debug/kotlin"
                getByName("release").kotlin.directories += "src/release/kotlin"
            }
            packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    open fun android(ns: String, closure: E.() -> Unit) {
        android(ns)
        closure(androidExtension)
    }
}
