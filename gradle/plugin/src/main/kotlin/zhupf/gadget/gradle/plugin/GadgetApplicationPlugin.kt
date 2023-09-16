package zhupf.gadget.gradle.plugin

import org.gradle.api.Project
import java.text.SimpleDateFormat

class GadgetApplicationPlugin : AndroidLibraryPlugin() {
    override fun apply(target: Project) {
        target.asAndroidApplication()
        super.apply(target)
        with(target) {
            applicationExtension.apply {
                defaultConfig {
                    applicationId = "zhupf.gadget"
                    versionName = SimpleDateFormat("YY.MM.dd").format(System.currentTimeMillis())
                    println("applicationId=$applicationId, versionName=$versionName")
                }
            }
            configureCommon()
        }
    }
}