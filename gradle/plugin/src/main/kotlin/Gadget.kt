import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

open class Gadget : Plugin<Project> {

    lateinit var project: Project; private set

    var configuration: Configuration = Configuration.Empty(this)
        internal set(value) {
            if (field is Configuration.Empty) {
                field = value
                field.configure()
                println("${project.name} configure")
            } else {
                throw IllegalArgumentException("configuration already set!")
            }
        }

    val dependency: Dependency = Dependency(this)

    override fun apply(target: Project) {
        this.project = target
        target.extensions.add(Gadget::class.java, "gadget", this)
    }
}

class GadgetApplication : Gadget() {
    override fun apply(target: Project) {
        target.pluginManager.apply(ANDROID_APPLICATION_ID)
        super.apply(target)
    }
}

class GadgetLibrary : Gadget() {
    override fun apply(target: Project) {
        target.pluginManager.apply(ANDROID_LIBRARY_ID)
        super.apply(target)
    }
}

class GadgetJvm : Gadget() {
    override fun apply(target: Project) {
        target.pluginManager.apply(KOTLIN_JVM_ID)
        target.pluginManager.apply(GROOVY_ID)
        super.apply(target)
    }
}


internal val Project.gadgetExtension: Gadget?
    get() = extensions.findByType(Gadget::class)