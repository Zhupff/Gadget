import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

internal class ThemeMerge(gadget: GadgetApplication) {
    init {
        gadget[ThemeMerge::class.java] = this
        gadget.project.appExtension.applicationVariants.all {
            gadget.project.appExtension.sourceSets.getByName(variantName).assets.srcDir(gadget.project.buildDir.resolve("themepacks${File.separator}${variantName}"))
            preBuildProvider.get().dependsOn(gadget.project.tasks.create("ThemeMerge$variantNameUpper"))
        }
    }
}


internal class ThemePack(gadget: GadgetApplication) {
    init {
        gadget[ThemePack::class.java] = this
        gadget.project.appExtension.applicationVariants.all {
            outputs.all {
                if (outputFile.name.endsWith(".apk")) {
                    val apkFile = outputFile
                    gadget.project.rootProject.allprojects.find {
                        it.gadgetExtension?.get(ThemeMerge::class.java) is ThemeMerge
                    }?.let { app ->
                        val task = gadget.project.tasks.register("ThemePack$variantNameUpper", ThemePackTask::class.java) {
                            inputFilePath = apkFile.path
                            outputFile = app.buildDir.resolve("themepacks${File.separator}${variantName}${File.separator}themepacks${File.separator}${gadget.project.name}")
                        }
                        app.tasks.named("ThemeMerge${variantNameUpper}") {
                            task.dependsOn("package$variantNameUpper")
                            dependsOn(task)
                        }
                    }
                }
            }
        }
    }
}


@CacheableTask
open class ThemePackTask : DefaultTask() {

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    val src = project.projectDir.resolve("src")

    @get:Input
    lateinit var inputFilePath: String

    @OutputFile
    lateinit var outputFile: File

    @TaskAction
    fun action() {
        val inputFile = File(inputFilePath)
        if (!inputFile.exists()) return
        Files.copy(inputFile.toPath(), outputFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING)
    }
}