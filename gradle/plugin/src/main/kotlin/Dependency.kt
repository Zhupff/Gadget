import org.gradle.api.Project

class Dependency internal constructor(val gadget: Gadget) {

    fun common() {
        gadget.project.let { project ->
            val libs = project.libs
            project.dependencies.apply {
                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("implementation", libs.findLibrary("androidx-appcompat").get())
            }
        }
    }

    fun modules(closure: @GradleScope Modules.() -> Unit = {}) {
        gadget.project.dependencies.add("implementation", gadget.findProject(":module"))
        closure(Modules())
    }

    inner class Modules internal constructor() {
        fun theme() {
            gadget.project.dependencies.add("implementation", gadget.findProject(":module:theme"))
        }

        fun widget() {
            gadget.project.dependencies.add("implementation", gadget.findProject(":module:widget"))
        }
    }

    fun components(closure: @GradleScope Components.() -> Unit) {
        closure(Components())
    }

    inner class Components internal constructor() {
        fun component(name: String, closure: @GradleScope Features.() -> Unit = {}) {
            if (gadget.project.name.startsWith("component")) {
                throw IllegalArgumentException("component-xxx can not depend on another component-xx")
            }
            gadget.project.dependencies.apply {
                add("implementation", gadget.findProject(":component-$name"))
            }
            closure(Features(name))
        }

        inner class Features internal constructor(val component: String) {

            fun feature() {
                gadget.project.dependencies.apply {
                    add("implementation", gadget.findProject(":component-$component:feature"))
                }
            }

            fun feature(name: String) {
                if (gadget.project.name.startsWith("feature")) {
                    throw IllegalArgumentException("feature-xxx can not depend on another feature-xx")
                }
                gadget.project.dependencies.apply {
                    add("implementation", gadget.findProject(":component-$component:feature-$name"))
                }
            }
        }
    }

    fun gadgets(closure: @GradleScope Gadgets.() -> Unit) {
        closure(Gadgets(gadget))
    }

    class Gadgets internal constructor(val gadget: Gadget) {

        fun common() {
            gadget.project.dependencies.apply {
                add("implementation", gadget.findProject(":gadgets:common"))
                add("implementation", gadget.findProject(":gadgets:common-android"))
            }
        }

        fun logger() {
            gadget.project.dependencies.apply {
                add("implementation", gadget.findProject(":gadgets:logger"))
            }
        }

        fun theme() {
            gadget.project.dependencies.apply {
                add("implementation", gadget.findProject(":gadgets:theme"))
            }
        }

        fun widget(compile: String? = null) {
            gadget.project.dependencies.apply {
                add("implementation", gadget.findProject(":gadgets:widget"))
                if (!compile.isNullOrEmpty()) {
                    add(compile, gadget.findProject(":gadgets:widget-compile"))
                }
            }
        }
    }
}


val <T : Gadget> T.dependency: Dependency?; get() = this[Dependency::class.java] as? Dependency

fun <T : Gadget> T.dependency(closure: @GradleScope Dependency.() -> Unit) {
    var dependency = this[Dependency::class.java] as? Dependency
    if (dependency == null) {
        dependency = Dependency(this)
        this[Dependency::class.java] = dependency
    }
    closure(dependency)
}

internal fun <T : Gadget> T.findProject(name: String): Project = project.rootProject.findProject(name)!!