package gadget.basic.compile

import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSTypeAlias
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.toTypeVariableName
import com.squareup.kotlinpoet.ksp.writeTo
import gadget.basic.annotation.DslScope
import gadget.basic.annotation.ViewDsl

class ViewDslKsp : AbstractKsp() {

    private val records = HashMap<Pair<String/* package name */, String/* file name */>, FileSpecInfo>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(ViewDsl::class.java.canonicalName)
            .filterIsInstance(KSDeclaration::class.java)
            .mapNotNull { symbol ->
                when (symbol) {
                    is KSClassDeclaration -> SymbolInfo.ClassSymbolInfo(symbol)
                    is KSTypeAlias -> SymbolInfo.AliasSymbolInfo(symbol)
                    is KSFunctionDeclaration -> SymbolInfo.FuncSymbolInfo(symbol)
                    else -> null
                }
            }.forEach { symbolInfo ->
                val packageName = symbolInfo.className.packageName
                val fileName = symbolInfo.className.toString()
                    .replace(packageName, "").replace('.', '_')
                    .let { it.substring(it.indexOfFirst { it != '_' }) }
                val fileSpecInfo = records.getOrPut(packageName to fileName) {
                    FileSpecInfo(FileSpec.builder(packageName, fileName))
                }
                var processed = false
                if (symbolInfo.superTypes.contains("android.view.View")) {
                    processed = processView(symbolInfo, fileSpecInfo) or processed
                }
                if (symbolInfo.superTypes.contains("android.view.ViewGroup.LayoutParams")) {
                    processed = processLayoutParams(symbolInfo, fileSpecInfo) or processed
                }
                if (processed) {
                    symbolInfo.symbol.containingFile?.let(fileSpecInfo.files::add)
                }
            }
        return emptyList()
    }

    override fun finish() {
        super.finish()
        records.values.forEach {
            it.builder.build().writeTo(processingEnv.codeGenerator, true, it.files)
        }
    }

    private fun processView(symbolInfo: SymbolInfo, fileSpecInfo: FileSpecInfo): Boolean {
        when (symbolInfo) {
            is SymbolInfo.ClassSymbolInfo,
            is SymbolInfo.AliasSymbolInfo -> {
                ViewDslBuilder.build(
                    symbolInfo, fileSpecInfo,
                    symbolInfo.symbol.typeParameters,
                    emptyList(),
                )
            }
            is SymbolInfo.FuncSymbolInfo -> {
                if (!symbolInfo.symbol.isConstructor() || !symbolInfo.symbol.isPublic()) {
                    return false
                }
                ViewDslBuilder.build(
                    symbolInfo, fileSpecInfo,
                    (symbolInfo.symbol.parentDeclaration as KSClassDeclaration).typeParameters,
                    symbolInfo.symbol.parameters,
                )
            }
        }
        return true
    }

    private fun processLayoutParams(symbolInfo: SymbolInfo, fileSpecInfo: FileSpecInfo): Boolean {
        return true
    }

    private sealed class SymbolInfo(
        open val symbol: KSDeclaration,
    ) {
        abstract val className: ClassName
        abstract val typeName: TypeName
        abstract val superTypes: List<String>
        val aliasName: String by lazy {
            symbol.annotations.find {
                it.annotationType.toTypeName() == ViewDsl::class.asTypeName()
            }!!.arguments.first().value.toString().ifBlank { className.simpleName }
        }

        class ClassSymbolInfo(
            override val symbol: KSClassDeclaration,
        ) : SymbolInfo(symbol) {
            override val className: ClassName = symbol.toClassName()
            override val typeName: TypeName = if (symbol.typeParameters.isNotEmpty()) {
                className.parameterizedBy(symbol.typeParameters.map { it.toTypeVariableName() })
            } else className
            override val superTypes: List<String> = symbol.getAllSuperTypes()
                .map { it.toClassName().toString() }
                .toList()
        }

        class AliasSymbolInfo(
            override val symbol: KSTypeAlias,
        ) : SymbolInfo(symbol) {
            override val className: ClassName
            override val typeName: TypeName
            override val superTypes: List<String>
            init {
                (symbol.type.resolve().declaration as KSClassDeclaration).let { type ->
                    className = type.toClassName()
                    typeName = if (type.typeParameters.isNotEmpty()) {
                        className.parameterizedBy(type.typeParameters.map { it.toTypeVariableName() })
                    } else className
                    superTypes = type.getAllSuperTypes()
                        .map { it.toClassName().toString() }
                        .toList()
                }
            }
        }

        class FuncSymbolInfo(
            override val symbol: KSFunctionDeclaration,
        ) : SymbolInfo(symbol) {
            override val className: ClassName
            override val typeName: TypeName
            override val superTypes: List<String>
            init {
                (symbol.parentDeclaration as KSClassDeclaration).let { type ->
                    className = type.toClassName()
                    typeName = if (type.typeParameters.isNotEmpty()) {
                        className.parameterizedBy(type.typeParameters.map { it.toTypeVariableName() })
                    } else className
                    superTypes = type.getAllSuperTypes()
                        .map { it.toClassName().toString() }
                        .toList()
                }
            }
        }
    }

    private class FileSpecInfo(
        val builder: FileSpec.Builder,
    ) {
        val files: HashSet<KSFile> = HashSet()
    }

    private object ViewDslBuilder {
        private fun lambda(symbolInfo: SymbolInfo): ParameterSpec {
            return ParameterSpec.builder(
                "lambda",
                LambdaTypeName.get(
                    receiver = symbolInfo.typeName
                        .copy(annotations = listOf(AnnotationSpec.builder(DslScope::class).build())),
                    parameters = listOf(ParameterSpec.unnamed(symbolInfo.typeName)),
                    returnType = UNIT,
                ),
            ).build()
        }

        fun build(
            symbolInfo: SymbolInfo,
            fileSpecInfo: FileSpecInfo,
            types: List<KSTypeParameter>,
            parameters: List<KSValueParameter>,
        ) {
            fileSpecInfo.builder.addFunction(
                FunSpec.builder(symbolInfo.aliasName.replaceFirstChar { it.uppercaseChar() })
                    .addModifiers(KModifier.INLINE)
                    .addTypeVariables(types.map { it.toTypeVariableName() })
                    .returns(symbolInfo.typeName)
                    .also { builder ->
                        if (symbolInfo is SymbolInfo.ClassSymbolInfo ||
                            symbolInfo is SymbolInfo.AliasSymbolInfo) {
                            fileSpecInfo.builder.addImport("gadget.basic.ui.dsl", "viewId")
                            builder.addParameter("context", "android.content.Context")
                                .addParameter(ParameterSpec.builder("id", STRING.copy(nullable = true)).defaultValue("null").build())
                                .addParameter("layoutParams", "android.view.ViewGroup.LayoutParams")
                                .addParameter(lambda(symbolInfo))
                                .addCode("""
                                    return ${symbolInfo.typeName}(context).also { view ->
                                      if (id != null) {
                                        view.id = id.viewId
                                      }
                                      view.layoutParams = layoutParams
                                      view.lambda(view)
                                    }
                                """.trimIndent())
                        } else if (symbolInfo is SymbolInfo.FuncSymbolInfo) {
                            builder.addParameters(parameters.map { p ->
                                ParameterSpec.builder(p.toString(), p.type.resolve().let {
                                    if (it.declaration is KSTypeParameter) {
                                        (it.declaration as KSTypeParameter).toTypeVariableName().copy(nullable = it.isMarkedNullable)
                                    } else {
                                        it.toTypeName()
                                    }
                                }).build()
                            })
                                .addParameter(lambda(symbolInfo))
                                .addCode("""
                                    return ${symbolInfo.typeName}(${parameters.toString().replace("[", "").replace("]", "")}).also { view ->
                                     view.lambda(view)
                                    }
                                """.trimIndent())
                        } else {
                            return
                        }
                    }
                    .build()
            ).addFunction(
                FunSpec.builder(symbolInfo.aliasName.replaceFirstChar { it.uppercaseChar() })
                    .addModifiers(KModifier.INLINE)
                    .receiver("android.view.ViewGroup")
                    .addTypeVariables(types.map { it.toTypeVariableName() })
                    .returns(symbolInfo.typeName)
                    .also { builder ->
                        if (symbolInfo is SymbolInfo.ClassSymbolInfo ||
                            symbolInfo is SymbolInfo.AliasSymbolInfo) {
                            fileSpecInfo.builder.addImport("gadget.basic.ui.dsl", "viewId")
                            builder.addParameter(ParameterSpec.builder("id", STRING.copy(nullable = true)).defaultValue("null").build())
                                .addParameter("layoutParams", "android.view.ViewGroup.LayoutParams")
                                .addParameter(lambda(symbolInfo))
                                .addCode("""
                                    return ${symbolInfo.typeName}(this.context).also { view ->
                                      if (id != null) {
                                        view.id = id.viewId
                                      }
                                      this.addView(view, layoutParams)
                                      view.lambda(view)
                                    }
                                """.trimIndent())
                        } else if (symbolInfo is SymbolInfo.FuncSymbolInfo) {
                            val firstParameter = parameters.first()
                            if (firstParameter.type.resolve().toClassName().toString() != "android.content.Context") {
                                error("First parameter should be android.content.Context")
                            }
                            val parametersWithoutContext = parameters.subList(1, parameters.size)
                            builder.addParameters(parametersWithoutContext.map { p ->
                                ParameterSpec.builder(p.toString(), p.type.resolve().let {
                                    if (it.declaration is KSTypeParameter) {
                                        (it.declaration as KSTypeParameter).toTypeVariableName().copy(nullable = it.isMarkedNullable)
                                    } else {
                                        it.toTypeName()
                                    }
                                }).build()
                            })
                                .addParameter(lambda(symbolInfo))
                                .addCode("""
                                    return ${symbolInfo.typeName}(this.context, ${parametersWithoutContext.toString().replace("[", "").replace("]", "")}).also { view ->
                                      this.addView(view)
                                      view.lambda(view)
                                    }
                                """.trimIndent())
                        } else {
                            return
                        }
                    }
                    .build()
            ).build()
        }
    }
}