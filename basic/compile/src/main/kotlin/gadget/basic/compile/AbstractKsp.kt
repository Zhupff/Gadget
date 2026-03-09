package gadget.basic.compile

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier

open class AbstractKsp : SymbolProcessorProvider, SymbolProcessor {

    protected lateinit var processingEnv: SymbolProcessorEnvironment
        private set

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = apply {
        this.processingEnv = environment
    }

    override fun process(resolver: Resolver): List<KSAnnotated> = emptyList()
}

fun FunSpec.Builder.addParameter(name: String, canonicalName: String, vararg modifiers: KModifier) =
    this.addParameter(name, ClassName("", canonicalName), *modifiers)

fun FunSpec.Builder.receiver(canonicalName: String, ) =
    this.receiver(ClassName("", canonicalName))
