package gadget.basic.log

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface Loggable {

    val loggableTag: String

    class Tag(
        private val singleton: Boolean = false,
    ) : Loggable, ReadOnlyProperty<Loggable, String> {
        private var tag: String? = null

        override val loggableTag: String by lazy {
            createTag(this)
            tag!!
        }

        override fun getValue(thisRef: Loggable, property: KProperty<*>): String {
            createTag(thisRef)
            return tag!!
        }

        private fun createTag(thisRef: Any) {
            if (this.tag != null) {
                return
            }
            this.tag = if (singleton) {
                thisRef::class.java.simpleName
            } else {
                "${thisRef::class.java.simpleName}(${thisRef.hashCode()})"
            }
        }
    }
}