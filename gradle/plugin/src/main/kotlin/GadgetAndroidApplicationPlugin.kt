import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

open class GadgetAndroidApplicationPlugin : GadgetAndroidPlugin<ApplicationExtension>() {

    override val androidExtension: ApplicationExtension
        get() = this.project.extensions.getByType(ApplicationExtension::class.java)

    override fun apply(target: Project) {
        super.apply(target)
        target.pluginManager.apply("com.android.application")
        target.pluginManager.apply("org.jetbrains.kotlin.android")
    }

    override fun android(namespace: String) {
        super.android(namespace)
        androidExtension.apply {
            defaultConfig.apply {
                targetSdk = 35
                ndk.abiFilters.add("arm64-v8a")
                productFlavors.forEach { f ->
                    f.applicationId = "${namespace}.${f.name}"
                    f.versionNameSuffix = "-${f.name}"
                }
            }
            signingConfigs {
                create("SIGNATURE") {
                    val signature = localProperties.getProperty("SIGNATURE")
                    storeFile = this@GadgetAndroidApplicationPlugin.project.rootProject.file("gradle/key-store")
                    keyAlias = signature
                    storePassword = signature
                    keyPassword = signature
                }
            }
            buildTypes {
                debug {
                    isMinifyEnabled = false
                    isShrinkResources = false
                    signingConfig = signingConfigs.getByName("SIGNATURE")
                }
                release {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    signingConfig = signingConfigs.getByName("SIGNATURE")
                    proguardFiles(
                        "proguard-rules.pro",
                        getDefaultProguardFile("proguard-android.txt"),
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                    )
                }
            }
        }
    }
}