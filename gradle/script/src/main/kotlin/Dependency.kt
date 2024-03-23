import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class Dependency<S : Script> internal constructor(
    val script: S,
) {

    init {
        assert(script[Dependency::class.java] == null)
        script[Dependency::class.java] = this
    }

    private val project: Project = script.project

    private val libs: VersionCatalog = project.libs

    private val dependencies: DependencyHandler = project.dependencies

    private fun project(name: String): Project = project.rootProject.findProject(name)!!

    fun common() {
        dependencies.apply {
            add("implementation", libs.findLibrary("androidx-core-ktx").get())
            add("implementation", libs.findLibrary("androidx-appcompat").get())
        }
    }

    fun gadget() {
        project.pluginManager.apply("org.jetbrains.kotlin.kapt")
        project.extensions.getByType(KaptExtension::class.java).arguments {
            arg("GROUP", project.group)
            arg("VERSION", project.version)
        }
        dependencies.apply {
            add("compileOnly", project.dependencies.gradleApi())
            add("compileOnly", project(":gadgets:api"))
            add("kapt", project(":gadgets:compile"))
        }
    }

    fun basic(
        closure: Basic.() -> Unit = {},
    ) {
        dependencies.add("implementation", project(":app:basic"))
        Basic().closure()
    }

    fun gadgets(
        closure: Gadgets.() -> Unit,
    ) {
        Gadgets().closure()
    }


    inner class Basic internal constructor() {
    }


    inner class Gadgets internal constructor() {

        fun basicJvm(method: String = "implementation") {
            dependencies.add(method, project(":gadgets:gadget-basic:basicJvm"))
        }

        fun basicAndroid(method: String = "implementation") {
            dependencies.add(method, project(":gadgets:gadget-basic:basicAndroid"))
        }

        fun logger(method: String = "implementation") {
            dependencies.add(method, project(":gadgets:gadget-logger:logger"))
        }

        fun toast(method: String = "implementation") {
            dependencies.add(method,  project(":gadgets:gadget-toast:toast"))
        }
    }
}



fun <S : Script> S.dependency(
    closure: Dependency<S>.() -> Unit = {},
) {
    Dependency(this).closure()
}