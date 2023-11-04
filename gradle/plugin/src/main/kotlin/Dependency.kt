import org.gradle.api.Project

class Dependency internal constructor(val gadget: Gadget) {

    fun module() {
        gadget.project.dependencies.add("implementation", gadget.findProject(":module"))
    }

    fun gadgets(closure: Gadgets.() -> Unit) {
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


fun <T : Gadget> T.dependency(closure: Dependency.() -> Unit) {
    closure(dependency)
}

fun <T : Gadget> T.findProject(name: String): Project = project.rootProject.findProject(name)!!