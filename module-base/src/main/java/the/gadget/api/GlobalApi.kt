package the.gadget.api

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class GlobalApi(val api: KClass<*>, val lazy: Boolean = true, vararg val dependencies: KClass<*>)