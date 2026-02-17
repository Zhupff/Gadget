package gadget.basic.theme

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import gadget.basic.logger.Loggable
import gadget.basic.logger.logW

abstract class AbstractThemeInflateFactory(
    protected val factory: LayoutInflater.Factory?,
) : LayoutInflater.Factory2, Loggable by Loggable.Tag() {

    protected val factory2: LayoutInflater.Factory2? = factory as? LayoutInflater.Factory2
    /** 对曾经解析过的主题资源进行缓存。 */
    protected open val resourceCaches: MutableMap<Int, Theme.Resource> = HashMap()
    /** 对曾经解析过的主题属性进行缓存。 */
    protected open val attributeCaches: MutableMap<String, Theme.Attribute> = HashMap()

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        var view = factory2?.onCreateView(parent, name, context, attrs) ?: factory?.onCreateView(name, context, attrs)
        val attribute2resource = parse(context, attrs) ?: return view
        if (view == null) {
            view = create(parent, name, context, attrs)
        }
        if (view != null) {
            val flagsAttribute = attribute2resource.remove(Theme.Attribute.Flags) as? Theme.Resource.Flags
            view = ThemeObserver(view, attribute2resource, flagsAttribute?.flags ?: 0).get()
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        var view = factory?.onCreateView(name, context, attrs)
        val attribute2resource = parse(context, attrs) ?: return view
        if (view == null) {
            view = create(null, name, context, attrs)
        }
        if (view != null) {
            val flagsAttribute = attribute2resource.remove(Theme.Attribute.Flags) as? Theme.Resource.Flags
            view = ThemeObserver(view, attribute2resource, flagsAttribute?.flags ?: 0).get()
        }
        return view
    }

    /**
     * 解析xml中使用到的主题资源和属性
     */
    protected open fun parse(context: Context, attrs: AttributeSet): MutableMap<Theme.Attribute, Theme.Resource>? {
        var result: MutableMap<Theme.Attribute, Theme.Resource>? = null
        for (index in 0 until attrs.attributeCount) {
            val attributeName = attrs.getAttributeName(index) ?: continue
            if (attrs.getAttributeNameResource(index) == R.attr.ThemeFlags) {
                val flags = attrs.getAttributeValue(index).substring(2).toIntOrNull(16) ?: continue
                if (result == null) {
                    result = HashMap()
                }
                result[Theme.Attribute.Flags] = Theme.Resource.Flags(flags)
            } else {
                val attribute = attributeCaches.getOrPut(attributeName) {
                    provide(attributeName) ?: Theme.Attribute.NotSupport
                }
                if (attribute === Theme.Attribute.NotSupport) {
                    continue
                }
                val attributeValue = attrs.getAttributeValue(index) ?: continue
                val resourceId = if (attributeValue.startsWith('@')) {
                    attributeValue.substring(1).toIntOrNull()
                } else { null } ?: continue
                val resource = resourceCaches.getOrPut(resourceId) {
                    try {
                        val resourceName = context.resources.getResourceEntryName(resourceId)
                        val resourceType = context.resources.getResourceTypeName(resourceId)
                        Theme.Resource(resourceId, resourceName, resourceType).let {
                            if (filter(it)) it else Theme.Resource.NotFound
                        }
                    } catch (throwable: Throwable) {
                        logW(throwable) { "parse failed!" }
                        Theme.Resource.NotFound
                    }
                }
                if (resource === Theme.Resource.NotFound) {
                    continue
                }
                if (result == null) {
                    result = HashMap()
                }
                result[attribute] = resource
            }
        }
        return result;
    }

    /**
     * 对解析到的疑似和主题相关的资源进行判断，返回true表示该资源有效，返回false会跳过该资源的处理。
     */
    protected open fun filter(resource: Theme.Resource): Boolean = when (resource.id) {
        gadget.basic.R.color.ThemePrimary,
        gadget.basic.R.color.ThemeOnPrimary,
        gadget.basic.R.color.ThemeBackground,
        gadget.basic.R.color.ThemeForeground,
        gadget.basic.R.color.ThemeSurface,
        gadget.basic.R.color.ThemeOutline,
        gadget.basic.R.color.ThemeError,
        gadget.basic.R.color.ThemeOnError -> true
        else -> false
    }

    /**
     * 根据属性名提供主题属性。
     */
    protected open fun provide(attributeName: String): Theme.Attribute? = when (attributeName) {
        else -> null
    }

    /**
     * 手动创建一个View。
     */
    protected open fun create(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return try {
            val layoutInflater = LayoutInflater.from(context)
            if (!name.contains('.'))
                layoutInflater.createView(name, "android.widget.", attrs)
                    ?: layoutInflater.createView(name, "android.view.", attrs)
                    ?: layoutInflater.createView(name, "android.webkit.", attrs)
            else
                layoutInflater.createView(name, null, attrs)
        } catch (e: Exception) {
            logW(e) { "Create failed!" }
            null
        }
    }
}