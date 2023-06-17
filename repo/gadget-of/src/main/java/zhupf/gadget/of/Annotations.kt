package zhupf.gadget.of

import kotlin.reflect.KClass


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ClassOf(vararg val value: KClass<*>)


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ObjectOf(vararg val value: KClass<*>) {
    companion object {
        @JvmField
        val FIELD_NAME: String = "INSTANCE"
    }
}