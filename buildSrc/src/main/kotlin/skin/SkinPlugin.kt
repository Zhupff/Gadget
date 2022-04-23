package skin

import android.databinding.tool.ext.toCamelCase
import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.res.GenerateLibraryRFileTask
import com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask
import com.android.utils.FileUtils
import com.squareup.javapoet.*
import groovy.xml.XmlSlurper
import isApp
import isLib
import logI
import org.gradle.api.DefaultTask
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import java.io.File
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicBoolean
import javax.lang.model.element.Modifier

open class SkinPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        when {
            project.isApp() -> {
                project.extensions.getByType(AppExtension::class.java).let {
                    registerTask(project, it.applicationVariants)
                }
            }
            project.isLib() -> {
                project.extensions.getByType(LibraryExtension::class.java).let {
                    registerTask(project, it.libraryVariants)
                }
            }
        }
    }

    open fun registerTask(project: Project, variants: DomainObjectSet<out BaseVariant>) {
        logI("$project apply ${javaClass.simpleName}.")
    }
}

class SkinResourcePlugin : SkinPlugin() {
    override fun apply(project: Project) {
        super.apply(project)
        if (project.isApp()) {
            project.extensions.getByType(AppExtension::class.java).sourceSets.getByName("main").assets.srcDir(project.buildDir.resolve(SKIN_ASSETS))
        }
    }

    override fun registerTask(project: Project, variants: DomainObjectSet<out BaseVariant>) {
        super.registerTask(project, variants)
        variants.all { variant ->
            val outputDir = project.buildDir.resolve("generated/source/gadget/${variant.dirName}")
            val packageName = XmlSlurper(false, false)
                .parse(variant.sourceSets.map { it.manifestFile }.first())
                .getProperty("@package")
                .toString()
            val once = AtomicBoolean(false)
            variant.outputs.all { output ->
                if (once.compareAndSet(false, true)) {
                    val task = output.processResourcesProvider.get()
                    val file = project.files(
                        when (task) {
                            is GenerateLibraryRFileTask -> task.getTextSymbolOutputFile()
                            is LinkApplicationAndroidResourcesTask -> task.getTextSymbolOutputFile()
                            else -> throw RuntimeException()
                        }
                    ).builtBy(task)
                    project.tasks.create("${SkinResourceTask::class.java.simpleName}${variant.name.toCamelCase()}", SkinResourceTask::class.java) {
                        it.outputDir = outputDir
                        it.rFile = file
                        it.packageName = packageName
                    }.also { variant.registerJavaGeneratingTask(it, outputDir) }
                    if (project.isApp()) {
                        project.tasks.create("${SkinPackageTask::class.java.simpleName}Anchor${variant.name.toCamelCase()}")
                            .also { variant.preBuildProvider.get().dependsOn(it) }
                    }
                }
            }
        }
    }
}

class SkinPackagePlugin : SkinPlugin() {
    override fun registerTask(project: Project, variants: DomainObjectSet<out BaseVariant>) {
        super.registerTask(project, variants)
        variants.all { variant ->
            val task = project.tasks.register("${SkinPackageTask::class.java.simpleName}${variant.name.toCamelCase()}", SkinPackageTask::class.java).get()
            project.rootProject.project(":app").tasks.named("${SkinPackageTask::class.java.simpleName}Anchor${variant.name.toCamelCase()}") {
                task.dependsOn("packageRelease")
                it.dependsOn(task)
            }
        }
    }
}

@CacheableTask
open class SkinResourceTask : DefaultTask() {
    @get:Input
    lateinit var packageName: String

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    lateinit var rFile: FileCollection

    @get:OutputDirectory
    lateinit var outputDir: File

    @TaskAction
    fun action() {
        logI("~~~~~~~~~~~~~~~~ action start ~~~~~~~~~~~~~~~~")
        val skinValues = mutableListOf<Pair<String, String>>()
        rFile.singleFile.forEachLine { line ->
            val values = line.split(' ')
            if (values.size == 4 &&
                values[0] == "int" &&
                values[1] in SUPPORTED_TYPES &&
                values[2].startsWith(SKIN_PREFIX)) {
                skinValues.add(values[1] to values[2].removePrefix(SKIN_PREFIX))
            }
        }
        if (!skinValues.isNullOrEmpty()) {
            SRBuilder(packageName, skinValues).build().writeTo(outputDir)
        }
        logI("~~~~~~~~~~~~~~~~ action over ~~~~~~~~~~~~~~~~")
    }
}

@CacheableTask
open class SkinPackageTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    val srcDir = project.projectDir.resolve("src")

    @OutputFile
    val outputFile = project.rootProject.project(":app").buildDir.resolve("${SKIN_ASSETS}/${project.name}.skin")

    @TaskAction
    fun action() {
        logI("~~~~~~~~~~~~~~~~ action start ~~~~~~~~~~~~~~~~")
        project.buildDir.resolve("outputs/apk/release").listFiles()
            ?.find { it.name.endsWith(".apk") }
            ?.let { FileUtils.copyFile(it, outputFile) }
        logI("~~~~~~~~~~~~~~~~ action over ~~~~~~~~~~~~~~~~")
    }
}

private class SRBuilder(
    private val packageName: String,
    private val skinValues: List<Pair<String, String>>
) {
    fun build(): JavaFile {
        val types = mutableMapOf<String, TypeSpec.Builder>()
        skinValues.forEach { (type, name) ->
            types.getOrPut(type) {
                TypeSpec.classBuilder(type.toCamelCase())
                    .superclass(BASE_OBSERVABLE)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            }.addField(
                FieldSpec.builder(Int::class.javaPrimitiveType, name)
                    .addModifiers(Modifier.PUBLIC)
                    .initializer("R.${type}.${SKIN_PREFIX}${name}")
                    .build()
            )
        }
        val SR = TypeSpec.classBuilder("SR")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(SKIN_RESOURCE)
            .addAnnotation(
                AnnotationSpec.builder(AUTO_SERVICE)
                    .addMember("value", "\$T.class", SKIN_RESOURCE)
                    .build()
            )
            .also {
                types.forEach { (type, typeSpec) ->
                    val typeName = ClassName.get("", type.toCamelCase())
                    it.addType(typeSpec.build())
                        .addField(
                            FieldSpec.builder(typeName, type, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                .initializer("new ${typeName.simpleName()}()")
                                .build()
                        )
                        .addMethod(
                            MethodSpec.methodBuilder("get${typeName.simpleName()}")
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                .returns(typeName)
                                .addStatement("return $type")
                                .build()
                        )
                }
            }
            .addMethod(
                MethodSpec.methodBuilder("notifyChange")
                    .addAnnotation(Override::class.java)
                    .addModifiers(Modifier.PUBLIC)
                    .also {
                        types.forEach { (type, _) ->
                            it.addStatement("${type}.notifyChange()")
                        }
                    }
                    .build()
            )
            .build()
        return JavaFile.builder(packageName, SR).build()
    }
}

private const val SKIN_ASSETS = "skin-assets"
private const val SKIN_PREFIX = "skin_"
private val SUPPORTED_TYPES = setOf("anim", "array", "attr", "bool", "color", "dimen",
    "drawable", "id", "integer", "layout", "menu", "plurals", "string", "style", "styleable")
private val SKIN_RESOURCE: ClassName; get() = ClassName.get("the.gadget.modulebase.skin", "SkinResource")
private val BASE_OBSERVABLE: ClassName; get() = ClassName.get("androidx.databinding", "BaseObservable")
private val AUTO_SERVICE: ClassName; get() = ClassName.get("com.google.auto.service", "AutoService")