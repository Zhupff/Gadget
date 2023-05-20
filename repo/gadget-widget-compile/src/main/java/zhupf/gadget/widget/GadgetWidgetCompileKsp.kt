package zhupf.gadget.widget

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

class GadgetWidgetCompileKsp : SymbolProcessorProvider, SymbolProcessor {

    private lateinit var environment: SymbolProcessorEnvironment
    private lateinit var processors: Array<Processor>

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = apply {
        this.environment = environment
        processors = arrayOf(
            WidgetDslProcessor(environment),
            LayoutParamsDslProcessor(environment),
        )
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        processors.forEach { it.process(resolver) }
        return emptyList()
    }

    override fun finish() {
        super.finish()
        processors.forEach { it.finish() }
    }


    private abstract class Processor(protected val env: SymbolProcessorEnvironment) {
        abstract fun process(resolver: Resolver)
        abstract fun finish()
    }


    private class WidgetDslProcessor(env: SymbolProcessorEnvironment) : Processor(env) {

        private val symbols = HashMap<String/* packageName|fileName */, HashSet<KSDeclaration>>()

        override fun process(resolver: Resolver) {
            resolver.getSymbolsWithAnnotation(WidgetDsl::class.java.canonicalName).forEach { symbol ->
                if (symbol is KSClassDeclaration) {
                    val packageName = symbol.declarePackageName
                    val fileName = symbol.declareClassName
                    symbols.getOrPut("${packageName}|${fileName}_WidgetDsl") { HashSet() }.add(symbol)
                } else if (symbol is KSDeclaration) {
                    val parentSymbol = symbol.parentDeclaration ?: return@forEach
                    val packageName = parentSymbol.declarePackageName
                    val fileName = parentSymbol.declareClassName
                    symbols.getOrPut("${packageName}|${fileName}_WidgetDsl") { HashSet() }.add(symbol)
                } else return@forEach
            }
        }

        override fun finish() {
            symbols.forEach { entry ->
                val (packageName, fileName) = entry.key.split('|')
                val imports = StringBuilder("package $packageName")
                    .appendLine()
                    .appendLine("import android.content.Context")
                    .appendLine("import android.view.ViewGroup")
                    .appendLine("import android.view.View")
                    .appendLine("import androidx.annotation.IdRes")
                    .appendLine("import zhupf.gadget.widget.*")
                val content = StringBuilder()
                entry.value.forEach { symbol ->
                    val typeName: String = if (symbol is KSClassDeclaration)
                        symbol.declareQualifiedName
                    else if (symbol is KSPropertyDeclaration)
                        symbol.type.resolve().declaration.declareQualifiedName
                    else return@forEach
                    val annotationValue = symbol.annotations.find {
                        it.annotationType.resolve().declaration.declareQualifiedName == WidgetDsl::class.java.canonicalName
                    }?.arguments?.find { it.name?.getShortName() == "value" }?.value as? String ?: ""
                    val functionName = annotationValue.ifEmpty { symbol.simpleName.getShortName() }
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
                try {
                    env.codeGenerator.createNewFile(
                        Dependencies(true, *(entry.value.mapNotNull { it.containingFile }.toTypedArray())),
                        packageName, fileName, "kt"
                    ).bufferedWriter(Charsets.UTF_8).use { writer ->
                        writer.write(imports.appendLine().append(content).toString())
                        writer.flush()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private class LayoutParamsDslProcessor(env: SymbolProcessorEnvironment) : Processor(env) {

        private val symbols = HashMap<String/* packageName|fileName */, HashSet<KSDeclaration>>()

        override fun process(resolver: Resolver) {
            resolver.getSymbolsWithAnnotation(LayoutParamsDsl::class.java.canonicalName).forEach { symbol ->
                if (symbol is KSClassDeclaration) {
                    val packageName = symbol.declarePackageName
                    val fileName = symbol.declareClassName
                    symbols.getOrPut("${packageName}|${fileName}_LayoutParamsDsl") { HashSet() }.add(symbol)
                } else if (symbol is KSDeclaration) {
                    val parentSymbol = symbol.parentDeclaration ?: return@forEach
                    val packageName = parentSymbol.declarePackageName
                    val fileName = parentSymbol.declareClassName
                    symbols.getOrPut("${packageName}|${fileName}_LayoutParamsDsl") { HashSet() }.add(symbol)
                } else return@forEach
            }
        }

        override fun finish() {
            symbols.forEach { entry ->
                val (packageName, fileName) = entry.key.split('|')
                val imports = StringBuilder("package $packageName")
                    .appendLine()
                    .appendLine("import android.view.View")
                    .appendLine("import zhupf.gadget.widget.*")
                val content = StringBuilder()
                entry.value.forEach { symbol ->
                    val typeName: String = if (symbol is KSClassDeclaration)
                        symbol.declareQualifiedName
                    else if (symbol is KSPropertyDeclaration)
                        symbol.type.resolve().declaration.declareQualifiedName
                    else return@forEach
                    val annotationValue = symbol.annotations.find {
                        it.annotationType.resolve().declaration.declareQualifiedName == LayoutParamsDsl::class.java.canonicalName
                    }?.arguments?.find { it.name?.getShortName() == "value" }?.value as? String ?: ""
                    val functionName = annotationValue.ifEmpty { symbol.simpleName.getShortName() }
                    content
                        .appendLine()
                        .appendLine("inline fun View.$functionName(")
                        .appendLine("    block: (@DslScope $typeName).() -> Unit,")
                        .appendLine("): $typeName = layoutParamsAs<$typeName>(block)")
                        .appendLine()
                }
                try {
                    env.codeGenerator.createNewFile(
                        Dependencies(true, *(entry.value.mapNotNull { it.containingFile }.toTypedArray())),
                        packageName, fileName, "kt"
                    ).bufferedWriter(Charsets.UTF_8).use { writer ->
                        writer.write(imports.appendLine().append(content).toString())
                        writer.flush()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}


val KSDeclaration.declarePackageName: String; get() = "${packageName.getQualifier()}.${packageName.getShortName()}"
val KSDeclaration.declareClassName: String; get() {
    var name = simpleName.getShortName()
    var declaration = parentDeclaration
    while (declaration != null) {
        name = "${declaration.simpleName.getShortName()}.$name"
        declaration = declaration.parentDeclaration
    }
    return name
}
val KSDeclaration.declareQualifiedName: String; get() = "$declarePackageName.$declareClassName"