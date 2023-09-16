package zhupf.gadget.gradle.plugin

import org.gradle.api.Project

class AndroidRepoPlugin : AndroidLibraryPlugin() {
    override fun apply(target: Project) {
        target.asAndroidLibrary()
        super.apply(target)
        with(target) {
            configurePublish()
        }
    }
}