package skin

import android.databinding.tool.ext.toCamelCase
import com.android.build.gradle.AppExtension
import com.android.utils.FileUtils
import isApp
import logI
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.*
import java.util.concurrent.atomic.AtomicBoolean

class SkinPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (project.isApp()) {
            logI("$project apply ${javaClass.simpleName}.")
            if (project.name == "app")
                registerAnchorTask(project)
            else
                registerPackageTask(project)
        } else {
            throw IllegalStateException("${javaClass.simpleName} can only apply on Application project.")
        }
    }

    private fun registerAnchorTask(project: Project) {
        project.extensions.getByType(AppExtension::class.java).let { extensions ->
            extensions.sourceSets.getByName("main").assets.srcDir(project.buildDir.resolve("skin-assets"))
            extensions.applicationVariants.all { variant ->
                val once = AtomicBoolean(false)
                variant.outputs.all { _ ->
                    if (once.compareAndSet(false, true)) {
                        project.tasks.create("${SkinPackageTask::class.java.simpleName}Anchor${variant.name.toCamelCase()}")
                            .also { variant.preBuildProvider.get().dependsOn(it) }
                    }
                }
            }
        }
    }

    private fun registerPackageTask(project: Project) {
        project.extensions.getByType(AppExtension::class.java).applicationVariants.all { variant ->
            val task = project.tasks.register("${SkinPackageTask::class.java.simpleName}${variant.name.toCamelCase()}", SkinPackageTask::class.java).get()
            project.rootProject.project(":app").tasks.named("${SkinPackageTask::class.java.simpleName}Anchor${variant.name.toCamelCase()}") {
                task.dependsOn("packageRelease")
                it.dependsOn(task)
            }
        }
    }
}

@CacheableTask
open class SkinPackageTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    val srcDir = project.projectDir.resolve("src")

    @OutputFile
    val outputFile = project.rootProject.project(":app").buildDir.resolve("skin-assets/${project.name}.skin")

    @TaskAction
    fun action() {
        logI("~~~~~~~~~~~~~~~~ action start ~~~~~~~~~~~~~~~~")
        project.buildDir.resolve("outputs/apk/release").listFiles()
            ?.find { it.name.endsWith(".apk") }
            ?.let { FileUtils.copyFile(it, outputFile) }
        logI("~~~~~~~~~~~~~~~~ action over ~~~~~~~~~~~~~~~~~")
    }
}