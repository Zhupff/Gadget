package zhupf.gadget.widget

import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.FileObject
import javax.tools.StandardLocation

class GadgetWidgetCompileApt : AbstractProcessor() {

    private val processors = arrayOf(
        WidgetDslProcessor(),
        LayoutParamsDslProcessor(),
    )

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes() = processors.flatMap { it.annotations() }.toSet()

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (roundEnv?.processingOver() != true) {
            processors.forEach { it.process(annotations, roundEnv, processingEnv) }
        } else {
            processors.forEach { it.finish(processingEnv) }
        }
        return false
    }



    private abstract class Processor {
        abstract fun annotations(): Set<String>
        abstract fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?, processorEnv: ProcessingEnvironment)
        abstract fun finish(processorEnv: ProcessingEnvironment)
        fun write(file: FileObject, content: String) {
            try {
                file.openWriter().use { writer ->
                    writer.write(content)
                    writer.flush()
                }
            } catch (e: Exception) {
                try {
                    file.delete()
                } finally {
                    throw e
                }
            }
        }
    }


    private class WidgetDslProcessor : Processor() {

        private val elements = HashMap<String/* packageName|fileName */, HashSet<Element>>()

        override fun annotations() = setOf(WidgetDsl::class.java.canonicalName)

        override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?, processorEnv: ProcessingEnvironment) {
            roundEnv?.getElementsAnnotatedWith(WidgetDsl::class.java)
                ?.forEach { element ->
                    val packageName = processorEnv.elementUtils.getPackageOf(element).qualifiedName
                    val fileName = if (element.kind.isField)
                        "${element.enclosingElement.simpleName}_WidgetDsl"
                    else if (element.kind.isClass)
                        "${element.simpleName}_WidgetDsl"
                    else
                        return@forEach
                    elements.getOrPut("$packageName|$fileName") { HashSet() }.add(element)
                }
        }

        override fun finish(processorEnv: ProcessingEnvironment) {
            elements.forEach { entry ->
                val (packageName, fileName) = entry.key.split('|')
                val imports = StringBuilder("package $packageName")
                    .appendLine()
                    .appendLine("import android.content.Context")
                    .appendLine("import android.view.ViewGroup")
                    .appendLine("import android.view.View")
                    .appendLine("import androidx.annotation.IdRes")
                    .appendLine("import zhupf.gadget.widget.*")
                val content = StringBuilder()
                entry.value.forEach { element ->
                    val typeName = element.asType().asTypeName().toString()
                    val functionName = element.getAnnotation(WidgetDsl::class.java).value.ifEmpty { element.simpleName }
                    content
                        .appendLine()
                        .appendLine("inline fun $functionName(")
                        .appendLine("    context: Context,")
                        .appendLine("    @IdRes id: Int = View.NO_ID,")
                        .appendLine("    size: Pair<Int, Int> = WRAP_CONTENT to WRAP_CONTENT,")
                        .appendLine("    block: (@DslScope $typeName).($typeName) -> Unit,")
                        .appendLine("): $typeName = $typeName(context).initialize(id, size, null).also { it.block(it) }")
                        .appendLine()
                        .appendLine("inline fun ViewGroup.$functionName(")
                        .appendLine("    @IdRes id: Int = View.NO_ID,")
                        .appendLine("    size: Pair<Int, Int> = WRAP_CONTENT to WRAP_CONTENT,")
                        .appendLine("    block: (@DslScope $typeName).($typeName) -> Unit,")
                        .appendLine("): $typeName = $typeName(context).initialize(id, size, this).also { it.block(it) }")
                        .appendLine()
                }
                write(processorEnv.filer.createResource(
                    StandardLocation.SOURCE_OUTPUT,
                    packageName, "${fileName}.kt",
                    *entry.value.toTypedArray()
                ), imports.appendLine().append(content).toString())
            }
        }
    }


    private class LayoutParamsDslProcessor : Processor() {

        private val elements = HashMap<String/* packageName|fileName */, HashSet<Element>>()

        override fun annotations(): Set<String> = setOf(LayoutParamsDsl::class.java.canonicalName)

        override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?, processorEnv: ProcessingEnvironment) {
            roundEnv?.getElementsAnnotatedWith(LayoutParamsDsl::class.java)
                ?.forEach { element ->
                    val packageName = processorEnv.elementUtils.getPackageOf(element).qualifiedName
                    val fileName = if (element.kind.isField)
                        "${element.enclosingElement.simpleName}_LayoutParamsDsl"
                    else if (element.kind.isClass)
                        "${element.simpleName}_LayoutParamsDsl"
                    else
                        return@forEach
                    elements.getOrPut("$packageName|$fileName") { HashSet() }.add(element)
                }
        }

        override fun finish(processorEnv: ProcessingEnvironment) {
            elements.forEach { entry ->
                val (packageName, fileName) = entry.key.split('|')
                val imports = StringBuilder("package $packageName")
                    .appendLine()
                    .appendLine("import android.view.View")
                    .appendLine("import zhupf.gadget.widget.*")
                val content = StringBuilder()
                entry.value.forEach { element ->
                    val typeName = element.asType().asTypeName().toString()
                    val functionName = element.getAnnotation(LayoutParamsDsl::class.java).value.ifEmpty { element.simpleName }
                    content
                        .appendLine()
                        .appendLine("inline fun View.$functionName(")
                        .appendLine("    block: (@DslScope $typeName).() -> Unit,")
                        .appendLine("): $typeName = layoutParamsAs<$typeName>(block)")
                        .appendLine()
                }
                write(processorEnv.filer.createResource(
                    StandardLocation.SOURCE_OUTPUT,
                    packageName, "${fileName}.kt",
                    *entry.value.toTypedArray()
                ), imports.appendLine().append(content).toString())
            }
        }
    }
}