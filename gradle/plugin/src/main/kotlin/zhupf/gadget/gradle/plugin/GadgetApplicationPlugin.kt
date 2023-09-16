package zhupf.gadget.gradle.plugin

import org.gradle.api.Project

class GadgetApplicationPlugin : AndroidLibraryPlugin() {
    override fun apply(target: Project) {
        target.asAndroidApplication()
        super.apply(target)
        with(target) {
            applicationExtension.apply {
                defaultConfig {
                    applicationId = "zhupf.gadget"
                    versionName = GADGET_VERSION
                    println("applicationId=$applicationId, versionName=$versionName")
                }
            }
            configureCommon()
        }
    }
}