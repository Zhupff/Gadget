package module

import android.databinding.tool.ext.toCamelCase
import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import groovy.xml.XmlSlurper
import isApp
import isLib
import logI
import org.gradle.api.DefaultTask
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.lang.model.element.Modifier

class ModulePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        logI("$project apply ${javaClass.simpleName}.")
        when {
            project.isApp() -> {
                project.extensions.getByType(AppExtension::class.java).let {
                    registerGenerateTask(project, it.applicationVariants)
                }
            }
            project.isLib() -> {
                project.extensions.getByType(LibraryExtension::class.java).let {
                    registerGenerateTask(project, it.libraryVariants)
                }
            }
            else -> {
                throw IllegalStateException("${javaClass.simpleName} can only apply on project with com.android.application or com.android.library!")
            }
        }
    }

    private fun registerGenerateTask(project: Project, variants: DomainObjectSet<out BaseVariant>) {
        variants.all { variant ->
            val outputDir = project.buildDir.resolve("generated/source/gadget/${variant.dirName}")
            val packageName = XmlSlurper(false, false)
                .parse(variant.sourceSets.map { it.manifestFile }.first())
                .getProperty("@package")
                .toString()
            project.tasks.create("${ModulePluginGenerateTask::class.java.simpleName}${variant.name.toCamelCase()}", ModulePluginGenerateTask::class.java) {
                it.outputDir = outputDir
                it.packageName = packageName
                it.moduleName = project.name
            }.also {
                variant.registerJavaGeneratingTask(it, outputDir)
            }
        }
    }
}


@CacheableTask
open class ModulePluginGenerateTask : DefaultTask() {

    @get:OutputDirectory
    lateinit var outputDir: File

    @get:Input
    lateinit var packageName: String

    @get:Input
    lateinit var moduleName: String

    @TaskAction
    fun action() {
        val typeSpec = TypeSpec.interfaceBuilder(moduleName.replace("-", "_").toCamelCase())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(MODULE)
                .addMember("moduleName", "\"${moduleName}\"")
                .build())
            .build()
        JavaFile.builder(packageName, typeSpec).build().writeTo(outputDir)
    }
}



private val MODULE: ClassName; get() = ClassName.get("the.gadget.libannotation", "Module")