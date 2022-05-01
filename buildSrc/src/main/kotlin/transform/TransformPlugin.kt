package transform

import com.android.build.api.transform.*
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.QualifiedContent.DefaultContentType.CLASSES
import com.android.build.api.transform.QualifiedContent.Scope
import com.android.build.api.transform.QualifiedContent.Scope.PROJECT
import com.android.build.api.transform.QualifiedContent.Scope.SUB_PROJECTS
import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.utils.FileUtils
import isApp
import isLib
import logI
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class TransformPlugin : Plugin<Project>, Transform() {

    private val transformers: List<Transformer> = listOf()

    private lateinit var context: TransformContext

    override fun apply(project: Project) {
        logI("$project apply ${javaClass.simpleName}.")
        when {
            project.isApp() -> {
                project.extensions.findByType(AppExtension::class.java)?.registerTransform(this)
            }
            project.isLib() -> {
                project.extensions.findByType(LibraryExtension::class.java)?.registerTransform(this)
            }
        }
    }

    override fun getName(): String = javaClass.simpleName

    override fun getInputTypes(): MutableSet<ContentType> = mutableSetOf(CLASSES)

    override fun getScopes(): MutableSet<in Scope> = mutableSetOf(PROJECT, SUB_PROJECTS)

    override fun isIncremental(): Boolean = true

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        transformInvocation ?: return
        context = TransformContext(transformInvocation)
        if (context.isIncremental) {
            transformInvocation.outputProvider.deleteAll()
        }
        beforeTransform(context)
        transformInvocation.inputs.forEach { input ->
            input.directoryInputs.forEach { handleDirInput(it, transformInvocation.outputProvider) }
            input.jarInputs.forEach { handleJarInput(it, transformInvocation.outputProvider) }
        }
        afterTransform(context)
    }

    private var beforeTransformTimestamp: Long = 0L

    private fun beforeTransform(context: TransformContext) {
        logI("beforeTransform, isIncremental=${context.isIncremental}")
        beforeTransformTimestamp = System.currentTimeMillis()
        transformers.forEach { it.beforeTransform(context) }
    }

    private var afterTransformTimestamp: Long = 0L

    private fun afterTransform(context: TransformContext) {
        transformers.forEach { it.afterTransform(context) }
        afterTransformTimestamp = System.currentTimeMillis()
        logI("afterTransform, duration=${afterTransformTimestamp - beforeTransformTimestamp}")
    }

    private fun handleDirInput(input: DirectoryInput, output: TransformOutputProvider) {
        logI("handleDirInput, input=${input.name}")
        val desDir = output.getContentLocation(input.name, input.contentTypes, input.scopes, Format.DIRECTORY)
        val desDirPath = desDir.absolutePath
        val srcDirPath = input.file.absolutePath
        if (context.isIncremental) {
            input.changedFiles
                .filter { (_, status) -> status != Status.NOTCHANGED }
                .forEach { (file, status) ->
                    logI("${file.name}'s status=${status}")
                    when (status) {
                        Status.REMOVED -> {
                            FileUtils.deleteIfExists(file)
                        }
                        Status.ADDED, Status.CHANGED -> {
                            val desFile = File(file.absolutePath.replace(srcDirPath, desDirPath))
                            if (file.isFile) {
                                if (!desFile.exists()) {
                                    desFile.parentFile?.let {
                                        if (!it.mkdirs() && !it.isDirectory) throw Exception("Can't create directory=${it.absolutePath}")
                                    } ?: throw Exception("$desFile's parentFile is null")
                                    desFile.createNewFile()
                                }
                                if (file.isClassFile) {
                                    val bytes = handleDirClass(file.absolutePath.removePrefix(srcDirPath).asClassName, file.readBytes())
                                    file.writeBytes(bytes)
                                }
                                FileUtils.copyFile(file, desFile)
                            }
                        }
                    }
                }
        } else {
            input.file.walk()
                .filter { it.isClassFile }
                .forEach {
                    val className = it.absolutePath.removePrefix(srcDirPath).asClassName
                    val bytes = handleDirClass(className, it.readBytes())
                    it.writeBytes(bytes)
                }
            FileUtils.copyDirectory(input.file, desDir)
        }
    }

    private fun handleJarInput(input: JarInput, output: TransformOutputProvider) {
        logI("handleJarInput, input=${input.name}${if (context.isIncremental) ", status=${input.status}" else ""}")
        if (context.isIncremental && input.status == Status.REMOVED) {
            FileUtils.deleteIfExists(input.file)
        }
        val tempFile = File("${context.tempDir.absolutePath}${File.separator}${input.name}_temp.jar")
        FileUtils.deleteIfExists(tempFile)
        JarFile(input.file).use { jarFile ->
            FileOutputStream(tempFile).use { fileOutputStream ->
                JarOutputStream(fileOutputStream).use { jarOutputStream ->
                    jarFile.entries().toList().forEach { jarEntry ->
                        jarFile.getInputStream(jarEntry).use { inputStream ->
                            var bytes = inputStream.readBytes()
                            if (jarEntry.name.endsWith(".class")) {
                                bytes = handleJarClass(jarEntry.name.asClassName, bytes)
                            }
                            jarOutputStream.putNextEntry(ZipEntry(jarEntry.name))
                            jarOutputStream.write(bytes)
                            jarOutputStream.closeEntry()
                        }
                    }
                }
            }
        }
        FileUtils.copyFile(tempFile, output.getContentLocation(input.name, input.contentTypes, input.scopes, Format.JAR))
        FileUtils.deleteIfExists(tempFile)
    }

    private fun handleDirClass(className: String, classBytes: ByteArray): ByteArray {
        var bytes = classBytes
        transformers.forEach { bytes = it.handleDirClass(className, classBytes) }
        return bytes
    }

    private fun handleJarClass(className: String, classBytes: ByteArray): ByteArray {
        var bytes = classBytes
        transformers.forEach { bytes = it.handleJarClass(className, classBytes) }
        return bytes
    }
}

private val String.asClassName: String; get() = replace("/", ".").replace("\\", ".").removePrefix(".")
private val File.isClassFile: Boolean; get() = this.isFile && name.endsWith(".class")