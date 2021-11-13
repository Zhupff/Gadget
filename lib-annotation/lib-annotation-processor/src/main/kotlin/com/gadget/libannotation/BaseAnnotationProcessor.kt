package com.gadget.libannotation

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

abstract class BaseAnnotationProcessor : AbstractProcessor() {

    final override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    final override fun getSupportedAnnotationTypes(): MutableSet<String> =
        getSupportedAnnotations().map { it.name }.toMutableSet()

    protected open fun getSupportedAnnotations(): Set<Class<*>> = emptySet()

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean =
        !annotations.isNullOrEmpty() && roundEnv != null
}