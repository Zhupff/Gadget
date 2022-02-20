package skin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.res.GenerateLibraryRFileTask
import com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask
import com.squareup.javapoet.*
import groovy.xml.XmlSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.json.JSONObject
import java.io.File
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicBoolean
import javax.lang.model.element.Modifier

class SkinPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("${javaClass.simpleName}(${hashCode()}) apply on ${project}!")
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
            val outputDir = project.buildDir.resolve("generated/source/skin/${variant.dirName}")
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
                    project.tasks.create("SRGenerateTask${variant.name.capitalize()}", GenerateTask::class.java) {
                        it.outputDir = outputDir
                        it.resFile = file
                        it.packageName = packageName
                        it.isAppModule = project.isApp()
                    }.also {
                        variant.registerJavaGeneratingTask(it, outputDir)
                    }
                }
            }
        }
    }
}


@CacheableTask
open class GenerateTask : DefaultTask() {

    @get:OutputDirectory
    var outputDir: File? = null

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    var resFile: FileCollection? = null

    @get:Input
    var packageName: String? = null

    @get:Input
    var isAppModule: Boolean? = null

    @TaskAction
    fun action() {
        val defaultSkinInfo = Skin.allSkinInfo.firstOrNull() ?: return
        val allSkinPrefix = Skin.allSkinInfo.map { it.prefix }
        val valueMap = mutableMapOf<String, MutableList<Array<String>>>()
        resFile!!.singleFile.forEachLine { line ->
            val values = line.split(' ')
            if (values.size == 4 && values[0] == "int" && values[1] in SUPPORTED_TYPES) {
                val prefix = values[2].startsWithAny(allSkinPrefix)
                if (!prefix.isNullOrEmpty()) {
                    valueMap.getOrPut(prefix) { mutableListOf() }
                        .add(arrayOf(values[1], values[2].removePrefix(prefix), values[3]))
                }
            }
        }
        if (valueMap.isNullOrEmpty()) return
        if (!valueMap[defaultSkinInfo.prefix].isNullOrEmpty()) {
            SRGenerator(packageName!!, defaultSkinInfo, valueMap[defaultSkinInfo.prefix]!!).build().writeTo(outputDir)
        }
        if (isAppModule == true) {
            valueMap.forEach { (prefix, values) ->
                SIGenerator(packageName!!, Skin.allSkinInfo.find { it.prefix == prefix }!!, values)
                    .build().writeTo(outputDir)
            }
        }
    }
}

class SRGenerator(
    private val packageName: String,
    private val defaultInfo: Skin.SkinInfo,
    private val values: List<Array<String>>
) {

    fun build(): JavaFile {
        val typeSpecs = mutableMapOf<String, TypeSpec.Builder>()
        values.forEach { (type, name, _) ->
            typeSpecs.getOrPut(type) {
                TypeSpec.classBuilder(type.capitalize())
                    .superclass(BASE_OBSERVABLE)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            }.addField(FieldSpec.builder(Int::class.javaPrimitiveType, name)
                .addModifiers(Modifier.PUBLIC)
                .initializer("R.${type}.${defaultInfo.prefix}${name}")
                .build())
        }

        val typeSpec = TypeSpec.classBuilder("SR")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(SKIN_RESOURCE)
            .addAnnotation(AnnotationSpec.builder(AUTO_SERVICE)
                .addMember("value", "\$T.class", SKIN_RESOURCE)
                .build())
            .also {
                typeSpecs.forEach { (type, spec) ->
                    val typeName = ClassName.get("", type.capitalize())
                    it.addType(spec.build())
                        .addField(FieldSpec.builder(typeName, type, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer("new ${typeName.simpleName()}()")
                            .build())
                        .addMethod(MethodSpec.methodBuilder("get${typeName.simpleName()}")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .returns(typeName)
                            .addStatement("return $type")
                            .build())
                }
            }
            .addMethod(MethodSpec.methodBuilder("notifyChange")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PUBLIC)
                .also {
                    typeSpecs.forEach { (type, _) ->
                        it.addStatement("${type}.notifyChange()")
                    }
                }
                .build())
            .build()

        return JavaFile.builder(packageName, typeSpec).build()
    }
}

class SIGenerator(
    private val packageName: String,
    private val info: Skin.SkinInfo,
    private val values: List<Array<String>>
) {

    fun build(): JavaFile {
        val typeFields = mutableMapOf<String, MutableList<Pair<String, String>>>()
        values.forEach { (type, name, value) ->
            typeFields.getOrPut(type) { mutableListOf() }.add(Pair(name, value))
        }
        val typeSpec = TypeSpec.classBuilder("${info.prefix.toUpperCase()}SI")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(SKIN_INFO)
            .addAnnotation(AnnotationSpec.builder(AUTO_SERVICE)
                .addMember("value", "\$T.class", SKIN_INFO)
                .build())
            .addMethod(MethodSpec.methodBuilder("initInfo")
                .addAnnotation(Override::class.java)
                .addModifiers(Modifier.PROTECTED)
                .returns(JSONObject::class.java)
                .addStatement("JSONObject json = new JSONObject()")
                .beginControlFlow("try")
                .addStatement("json.put(\"id\", ${info.id})")
                .addStatement("json.put(\"name\", \"${info.name}\")")
                .addStatement("json.put(\"prefix\", \"${info.prefix}\")")
                .nextControlFlow("catch(\$T e)", Exception::class.java)
                .addStatement("e.printStackTrace()")
                .addStatement("return new JSONObject()")
                .endControlFlow()
                .addStatement("return json")
                .build())
            .build()
        return JavaFile.builder(packageName, typeSpec).build()
    }
}



private val SUPPORTED_TYPES = setOf("anim", "array", "attr", "bool", "color", "dimen",
    "drawable", "id", "integer", "layout", "menu", "plurals", "string", "style", "styleable")
private val SKIN_RESOURCE: ClassName; get() = ClassName.get("the.gadget.modulebase.skin", "SkinResource")
private val SKIN_INFO: ClassName; get() = ClassName.get("the.gadget.modulebase.skin", "SkinInfo")
private val BASE_OBSERVABLE: ClassName; get() = ClassName.get("androidx.databinding", "BaseObservable")
private val AUTO_SERVICE: ClassName; get() = ClassName.get("com.google.auto.service", "AutoService")

private fun Project.isApp(): Boolean = plugins.hasPlugin("com.android.application")
private fun Project.isLib(): Boolean = plugins.hasPlugin("com.android.library")
private fun String.startsWithAny(prefixes: Collection<String>?): String? {
    prefixes?.forEach { if (startsWith(it)) return it }
    return null
}