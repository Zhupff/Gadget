package zhupf.gadget.of

import kotlin.reflect.KClass


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ClassOf(vararg val value: KClass<*>)


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ObjectOf(vararg val value: KClass<*>)