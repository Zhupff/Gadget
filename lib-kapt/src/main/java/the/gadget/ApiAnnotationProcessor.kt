package the.gadget

import com.google.auto.common.SuperficialValidation
import com.google.auto.service.AutoService
import the.gadget.api.GlobalApi
import the.gadget.api.GlobalDebugApi
import the.gadget.api.ScopeApi
import the.gadget.api.ScopeDebugApi
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation

@AutoService(Processor::class)
class ApiAnnotationProcessor : BaseAnnotationProcessor() {

    private val globalApiFile = "META-INF/services/${GlobalApi::class.java.canonicalName}"

    private val scopeApiFile = "META-INF/services/${ScopeApi::class.java.canonicalName}"

    private val globalDebugApiFile = "META-INF/services/${GlobalDebugApi::class.java.canonicalName}"

    private val scopeDebugApiFile = "META-INF/services/${ScopeDebugApi::class.java.canonicalName}"

    private val globalApis = mutableSetOf<String>()

    private val scopeApis = mutableSetOf<String>()

    private val globalDebugApis = mutableSetOf<String>()

    private val scopeDebugApis = mutableSetOf<String>()

    override fun getSupportedAnnotationTypes(): Set<String> = setOf(
        GlobalApi::class.java.name, ScopeApi::class.java.name
    )

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (roundEnv?.processingOver() != true) {
            collect(roundEnv, globalApis, GlobalApi::class.java)
            collect(roundEnv, scopeApis, ScopeApi::class.java)
            collect(roundEnv, globalDebugApis, GlobalDebugApi::class.java)
            collect(roundEnv, scopeDebugApis, ScopeDebugApi::class.java)
        } else {
            generate(globalApis, globalApiFile)
            generate(scopeApis, scopeApiFile)
            generate(globalDebugApis, globalDebugApiFile)
            generate(scopeDebugApis, scopeDebugApiFile)
        }
        return false
    }

    private fun collect(roundEnv: RoundEnvironment?, apis: MutableSet<String>, annotation: Class<out Annotation>) {
        roundEnv?.getElementsAnnotatedWith(annotation)
            ?.filter { SuperficialValidation.validateElement(it) }
            ?.forEach { apis.add(it.canonicalName) }
    }

    private fun generate(apis: MutableSet<String>, file: String) {
        if (apis.isNullOrEmpty()) return
        processingEnv.filer.createResource(StandardLocation.CLASS_OUTPUT, "", file)
            .openWriter().use { writer ->
                apis.forEach {
                    writer.appendLine(it)
                }
                writer.flush()
            }
    }
}