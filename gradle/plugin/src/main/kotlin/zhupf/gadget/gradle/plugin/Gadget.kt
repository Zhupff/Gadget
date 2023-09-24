package zhupf.gadget.gradle.plugin

import org.gradle.api.Project

class Gadget internal constructor(internal val target: Project) {

    fun repo(script: GadgetRepo.() -> Unit) {
        script(GadgetRepo(target))
    }

    class GadgetRepo(internal val target: Project) {
        fun widget(compile: String = "kapt", depend: String = "implementation") {
            with(target) {
                pluginManager.apply(
                    when (compile) {
                        "ksp" -> "com.google.devtools.ksp"
                        "apt", "kapt" -> "org.jetbrains.kotlin.kapt"
                        else -> throw IllegalArgumentException("Can't find compile method called $compile.")
                    }
                )
                dependencies.add(depend, rootProject.findProject(":repo:widget")!!)
                dependencies.add(compile, rootProject.findProject(":repo:widget-compile")!!)
            }
        }
    }
}

fun Project.gadget(script: Gadget.() -> Unit) {
    script(Gadget(this))
}