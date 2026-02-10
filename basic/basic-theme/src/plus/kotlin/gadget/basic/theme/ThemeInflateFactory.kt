package gadget.basic.theme

import android.view.LayoutInflater

class ThemeInflateFactory(
    factory: LayoutInflater.Factory? = null,
) : AbstractThemeInflateFactory(factory) {

    private companion object {
        @JvmStatic
        val RESOURCE_CACHES = HashMap<Int, Theme.Resource>()
        @JvmStatic
        val ATTRIBUTE_CACHES = HashMap<String, Theme.Attribute>()
    }

    override val resourceCaches: MutableMap<Int, Theme.Resource> = RESOURCE_CACHES

    override val attributeCaches: MutableMap<String, Theme.Attribute> = ATTRIBUTE_CACHES
}