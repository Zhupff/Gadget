import org.gradle.api.Project

class Dependency internal constructor(val gadget: Gadget) {

    fun module() {
        gadget.project.dependencies.add("implementation", gadget.findProject(":module"))
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

        fun widget(compile: String? = "ksp") {
            gadget.project.dependencies.apply {
                add("implementation", gadget.findProject(":gadgets:widget"))
                if (!compile.isNullOrEmpty()) {
                    add(compile, gadget.findProject(":gadgets:widget-compile"))
                }
            }
        }
    }
}


fun <T : Gadget> T.dependency(closure: @GradleScope Dependency.() -> Unit) {
    closure(dependency)
}

internal fun <T : Gadget> T.findProject(name: String): Project = project.rootProject.findProject(name)!!