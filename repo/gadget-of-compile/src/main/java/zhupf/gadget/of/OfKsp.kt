package zhupf.gadget.of

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType
import java.lang.Exception

class OfKsp : SymbolProcessorProvider, SymbolProcessor {

    private lateinit var environment: SymbolProcessorEnvironment

    private val ksFileMap = HashMap<String, HashSet<KSFile>>()
    private val contentMap = HashMap<String, HashSet<String>>()

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = apply {
        this.environment = environment
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        arrayOf(ClassOf::class.java, ObjectOf::class.java).forEach { cls ->
            resolver.getSymbolsWithAnnotation(cls.canonicalName)
                .mapNotNull { it as? KSClassDeclaration }
                .forEach { symbol ->
                    if (cls == ClassOf::class.java && symbol.classKind != ClassKind.CLASS) {
                        throw IllegalStateException("${symbol.declareQualifiedName} is not a class.")
                    }
                    if (cls == ObjectOf::class.java && symbol.classKind != ClassKind.OBJECT) {
                        throw IllegalStateException("${symbol.declareQualifiedName} is not a object.")
                    }
                    symbol.annotations.find {
                        it.annotationType.resolve().declaration.declareQualifiedName == cls.canonicalName
                    }?.arguments?.find {
                        it.name?.getShortName() == "value"
                    }?.value?.asIterable?.filterIsInstance(KSType::class.java)?.forEach { of ->
                        val ofName = of.declaration.declareQualifiedName
                        ksFileMap.getOrPut(ofName) { HashSet() }.addIfNonNull(symbol.containingFile)
                        contentMap.getOrPut(ofName) { HashSet() }.add("${cls.simpleName}-${symbol.declareQualifiedName}")
                    }
                }
        }
        return emptyList()
    }

    override fun finish() {
        ksFileMap.forEach { (of, ksFiles) ->
            contentMap[of]?.let { contents ->
                val content = StringBuilder().also { sb ->
                    contents.forEach { line -> sb.appendLine(line) }
                }
                try {
                    environment.codeGenerator.createNewFile(
                        Dependencies(true, *ksFiles.toTypedArray()),
                        "", "META-INF/services/${of}_OF", ""
                    ).bufferedWriter(Charsets.UTF_8).use { writer ->
                        writer.write(content.toString())
                        writer.flush()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private val KSDeclaration.declarePackageName: String; get() = "${packageName.getQualifier()}.${packageName.getShortName()}"
    private val KSDeclaration.declareClassName: String; get() {
        var name = simpleName.getShortName()
        var declaration = parentDeclaration
        while (declaration != null) {
            name = "${declaration.simpleName.getShortName()}.$name"
            declaration = declaration.parentDeclaration
        }
        return name
    }
    private val KSDeclaration.declareQualifiedName: String; get() = "$declarePackageName.$declareClassName"
    private val Any.asIterable: Iterable<*>?; get() = this as? Iterable<*>
    private fun <T> HashSet<T>.addIfNonNull(item: T?) { if (item != null) add(item) }
}