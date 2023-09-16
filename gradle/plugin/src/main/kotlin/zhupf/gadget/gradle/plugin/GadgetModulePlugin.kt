package zhupf.gadget.gradle.plugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class GadgetModulePlugin : AndroidLibraryPlugin() {
    override fun apply(target: Project) {
        target.asAndroidLibrary()
        super.apply(target)
        with(target) {
            configureCommon()
            findProject(":app")?.dependencies {
                "implementation"(target)
            }
        }
    }
}