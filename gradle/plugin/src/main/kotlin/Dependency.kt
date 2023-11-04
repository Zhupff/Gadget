import org.gradle.api.Project

class Dependency internal constructor(val gadget: Gadget) {

    private fun findProject(name: String): Project = gadget.project.rootProject.findProject(":$name")!!

    fun module() {
        gadget.project.dependencies.add("implementation", findProject("module"))
    }
}


fun <T : Gadget> T.dependency(closure: Dependency.() -> Unit) {
    closure(dependency)
}