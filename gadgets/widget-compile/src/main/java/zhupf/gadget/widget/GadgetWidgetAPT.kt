package zhupf.gadget.widget

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

class GadgetWidgetAPT : AbstractProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes() = setOf(
        WidgetX::class.java.canonicalName,
        WidgetDsl::class.java.canonicalName,
        LayoutParamsDsl::class.java.canonicalName,
    )

    override fun process(annotations: Set<TypeElement>?, roundEnvironment: RoundEnvironment?): Boolean {
        return false
    }
}