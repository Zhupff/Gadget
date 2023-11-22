package zhupf.gadget.theme.attribute

import android.view.View
import zhupf.gadget.theme.Theme
import zhupf.gadget.theme.ThemeAttribute

class BackgroundAttribute : ThemeAttribute("background") {

    override fun apply(view: View, theme: Theme) {
        when (resourceType) {
            TYPE_COLOR -> {
                val colorStateList = theme.getColorStateList(resourceId, resourceName, resourceType)
                if (colorStateList != null) {
                    view.backgroundTintList = colorStateList
                } else {
                    view.setBackgroundColor(theme.getColor(resourceId, resourceName, resourceType))
                }
            }
            TYPE_DRAWABLE -> {
                view.background = theme.getDrawable(resourceId, resourceName, resourceType)
            }
        }
    }
}