package gadget.basic.theme

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import gadget.basic.exception.throws

interface Theme {

    val id: String

    fun getColor(r: Resource): Int

    fun getDrawable(r: Resource): Drawable?

    fun getString(r: Resource, vararg args: Any): String?


    /**
     * 主题属性，原型是按照View体系的属性设计的，可按照实际的使用场景赋予其不同的含义。
     */
    abstract class Attribute(
        /** 属性名[android.util.AttributeSet.getAttributeName] */
        val name: String,
    ) {
        object NotSupport : Attribute("NotSupport") {
            override fun apply(view: View, theme: Theme, resource: Resource) {
                IllegalStateException("Attribute Not Supported!").throws()
            }
        }

        object Flags : Attribute("ThemeFlags") {
            override fun apply(view: View, theme: Theme, resource: Resource) {
                IllegalStateException("Should be removed after parse!").throws()
            }
        }

        /**
         * 对视图[view]的当前属性使用主题[theme]里的资源[resource]进行改变。
         */
        abstract fun apply(view: View, theme: Theme, resource: Resource)

        override fun hashCode(): Int = name.hashCode()

        override fun equals(other: Any?): Boolean = other is Attribute && this.name == other.name

        override fun toString(): String = "Attribute[${name}]"
    }


    /**
     * 主题资源三元组，原型是按照View体系的资源设计的，可按照实际的使用场景赋予其不同的含义。
     */
    open class Resource(
        /** 资源id。 */
        val id: Int,
        /** 资源名[android.content.res.Resources.getResourceEntryName]。 */
        val name: String,
        /** 资源名[android.content.res.Resources.getResourceTypeName]。 */
        val type: String,
    ) {
        companion object {
            const val TYPE_COLOR = "color"
            const val TYPE_DRAWABLE = "drawable"
            const val TYPE_STRING = "string"
        }

        object NotFound : Resource(ResourcesCompat.ID_NULL, "NotFound", "NotFound")

        class Flags(val flags: Int) : Resource(R.attr.ThemeFlags, "ThemeFlags", "attr")

        override fun hashCode(): Int = id

        override fun equals(other: Any?): Boolean = other is Resource && this.id == other.id

        override fun toString(): String = "Resource[${id},${name},${type}]"
    }
}