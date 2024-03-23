import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class Script : Plugin<Project>, MutableMap<Any, Any> by HashMap() {

    val project: Project; get() = this[Project::class.java] as Project

    abstract val configuration: Configuration<*>

    abstract val dependency: Dependency<*>

    override fun apply(target: Project) {
        clear()
        this[Project::class.java] = target
    }
}



class ApplicationScript : Script() {

    override val configuration: Configuration.AndroidConfiguration.ApplicationConfiguration
        get() = this[Configuration::class.java] as Configuration.AndroidConfiguration.ApplicationConfiguration

    override val dependency: Dependency<ApplicationScript>
        get() = this[Dependency::class.java] as Dependency<ApplicationScript>

    override fun apply(target: Project) {
        super.apply(target)
        target.pluginManager.apply("com.android.application")
        target.pluginManager.apply("org.jetbrains.kotlin.android")
        target.extensions.add(ApplicationScript::class.java, "script", this)
    }
}



class LibraryScript : Script() {

    override val configuration: Configuration.AndroidConfiguration.LibraryConfiguration
        get() = this[Configuration::class.java] as Configuration.AndroidConfiguration.LibraryConfiguration

    override val dependency: Dependency<LibraryScript>
        get() = this[Dependency::class.java] as Dependency<LibraryScript>

    override fun apply(target: Project) {
        super.apply(target)
        target.pluginManager.apply("com.android.library")
        target.pluginManager.apply("org.jetbrains.kotlin.android")
        target.extensions.add(LibraryScript::class.java, "script", this)
    }
}



class JvmScript : Script() {

    override val configuration: Configuration.JvmConfiguration
        get() = this[Configuration::class.java] as Configuration.JvmConfiguration

    override val dependency: Dependency<JvmScript>
        get() = this[Dependency::class.java] as Dependency<JvmScript>

    override fun apply(target: Project) {
        super.apply(target)
        target.pluginManager.apply("org.jetbrains.kotlin.jvm")
        target.pluginManager.apply("java-library")
        target.pluginManager.apply("groovy")
        target.extensions.add(JvmScript::class.java, "script", this)
    }
}