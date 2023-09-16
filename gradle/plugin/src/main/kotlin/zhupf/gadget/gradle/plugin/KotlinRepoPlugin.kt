package zhupf.gadget.gradle.plugin

import org.gradle.api.Project

class KotlinRepoPlugin : KotlinLibraryPlugin() {
    override fun apply(target: Project) {
        super.apply(target)
        with(target) {
            configurePublish()
        }
    }
}