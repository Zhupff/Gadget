import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

abstract class Gadget : Plugin<Project>, MutableMap<Any, Any> by HashMap(4) {

    lateinit var project: Project; private set

    override fun apply(target: Project) {
        clear()
        this.project = target
    }
}

class GadgetApplication : Gadget() {
    override fun apply(target: Project) {
        super.apply(target)
        target.pluginManager.apply(ANDROID_APPLICATION_ID)
        target.extensions.add(GadgetApplication::class.java, "gadget", this)
    }
}

class GadgetLibrary : Gadget() {
    override fun apply(target: Project) {
        super.apply(target)
        target.pluginManager.apply(ANDROID_LIBRARY_ID)
        target.extensions.add(GadgetLibrary::class.java, "gadget", this)
    }
}

class GadgetJvm : Gadget() {
    override fun apply(target: Project) {
        super.apply(target)
        target.pluginManager.apply(KOTLIN_JVM_ID)
        target.pluginManager.apply(GROOVY_ID)
        target.extensions.add(GadgetJvm::class.java, "gadget", this)
    }
}


internal val Project.gadgetExtension: Gadget?
    get() = extensions.findByType(Gadget::class)