package zhupf.gadget.theme

import zhupf.gadget.theme.attribute.BackgroundAttribute

open class ThemeConfig(
    val prefix: String,
    val attributes: List<ThemeAttribute>,
) {
    companion object {
        val COMMON_ATTRIBUTES: List<ThemeAttribute>; get() = listOf(
            BackgroundAttribute(),
        )
    }

    protected val attributesMap = attributes.associateBy { it.attributeName }

    open fun obtainAttribute(attributeName: String, resourceId: Int, resourceName: String, resourceType: String): ThemeAttribute? =
        attributesMap[attributeName]?.copy(resourceId, resourceName, resourceType)
}