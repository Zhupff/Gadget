package zhupf.gadget.of

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation

class OfApt : AbstractProcessor() {
    private val elementMap = HashMap<String, HashSet<Element>>()
    private val contentMap = HashMap<String, HashSet<String>>()

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes() = setOf(
        ClassOf::class.java.canonicalName,
        ObjectOf::class.java.canonicalName,
    )

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (roundEnv?.processingOver() != true) {
            arrayOf(ClassOf::class.java, ObjectOf::class.java).forEach { cls ->
                roundEnv?.getElementsAnnotatedWith(cls)
                    ?.forEach { element ->
                        if (element.kind != ElementKind.CLASS) {
                            throw IllegalStateException("${element.canonicalName} is not a class.")
                        }
                        val content = "${cls.simpleName}-${element.canonicalName}"
                        element.annotationMirrors.find {
                            it.annotationType.asElement().canonicalName == cls.canonicalName
                        }?.elementValues?.forEach { k, v ->
                            if (k.simpleName.toString() == "value") {
                                (v.value as Iterable<*>).filterIsInstance(AnnotationValue::class.java)
                                    .forEach { of ->
                                        elementMap.getOrPut(of.value.toString()) { HashSet() }.add(element)
                                        contentMap.getOrPut(of.value.toString()) { HashSet() }.add(content)
                                    }
                            }
                        }
                    }
            }
        } else {
            elementMap.forEach { (of, elements) ->
                contentMap[of]?.let { contents ->
                    val content = StringBuilder().also { sb ->
                        contents.forEach { line -> sb.appendLine(line) }
                    }
                    val file = processingEnv.filer.createResource(
                        StandardLocation.CLASS_OUTPUT,
                        "", "META-INF/services/${of}_OF",
                        *elements.toTypedArray()
                    )
                    try {
                        file.openWriter().use { writer ->
                            writer.write(content.toString())
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
        }
        return false
    }

    private val Element.canonicalName; get() = "${processingEnv.elementUtils.getPackageOf(this).qualifiedName}.${simpleName}"
}