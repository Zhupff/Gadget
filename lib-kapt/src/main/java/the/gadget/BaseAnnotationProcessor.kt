package the.gadget

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

abstract class BaseAnnotationProcessor : AbstractProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes(): Set<String> = setOf()

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean = false

    protected val projectName: String by lazy { processingEnv.options["projectName"]!! }

    protected val projectNameCamelCase: String by lazy {
        projectName.let { projectName ->
            val stringBuilder = StringBuilder()
            projectName.replace("-", "_")
                .split("_")
                .forEach { str ->
                    stringBuilder.append(str.replaceFirstChar { it.uppercaseChar() })
                }
            stringBuilder.toString()
        }
    }

    protected val projectPackage: String by lazy { processingEnv.options["projectPackage"]!! }

    protected val Element.canonicalName; get() = "${processingEnv.elementUtils.getPackageOf(this).qualifiedName}.${simpleName}"
}