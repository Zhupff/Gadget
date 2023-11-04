package zhupf.gadget.widget

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated

class GadgetWidgetKSP : SymbolProcessorProvider, SymbolProcessor {

    private lateinit var environment: SymbolProcessorEnvironment

    override fun create(environment: SymbolProcessorEnvironment) = apply { this.environment = environment }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        return emptyList()
    }
}