import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

open class GadgetAndroidApplicationPlugin : GadgetAndroidPlugin<ApplicationExtension>() {

    override val androidExtension: ApplicationExtension
        get() = project.extensions.getByType(ApplicationExtension::class.java)

    override fun apply(target: Project) {
        super.apply(target)
        target.pluginManager.apply("com.android.application")
        target.pluginManager.apply("org.jetbrains.kotlin.android")
    }

    override fun android(ns: String) {
        super.android(ns)
        androidExtension.apply {
            defaultConfig {
                targetSdk = 35
                ndk.abiFilters += "arm64-v8a"
            }
        }
    }
}
