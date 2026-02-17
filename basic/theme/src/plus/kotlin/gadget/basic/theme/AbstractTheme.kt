package gadget.basic.theme

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import gadget.basic.Gadget
import gadget.basic.exception.throws
import gadget.basic.logger.Loggable
import gadget.basic.logger.logW

internal abstract class AbstractTheme<PARENT : Theme>(
    val name: String,
    protected var parent: PARENT? = null,
) : Theme, Loggable {

    abstract val primaryColor: Int
    abstract val onPrimaryColor: Int
    abstract val backgroundColor: Int
    abstract val foregroundColor: Int
    abstract val surfaceColor: Int
    abstract val outlineColor: Int
    abstract val errorColor: Int
    abstract val onErrorColor: Int

    override fun getColor(r: Theme.Resource): Int = when (r.id) {
        gadget.basic.R.color.ThemePrimary    -> primaryColor
        gadget.basic.R.color.ThemeOnPrimary  -> onPrimaryColor
        gadget.basic.R.color.ThemeBackground -> backgroundColor
        gadget.basic.R.color.ThemeForeground -> foregroundColor
        gadget.basic.R.color.ThemeSurface    -> surfaceColor
        gadget.basic.R.color.ThemeOutline    -> outlineColor
        gadget.basic.R.color.ThemeError      -> errorColor
        gadget.basic.R.color.ThemeOnError    -> onErrorColor
        else -> parent?.getColor(r)
            ?: if (Gadget.debuggable) {
                IllegalStateException("getColor($r) from $name failed!").throws()
            } else {
                logW(null) { "getColor($r) failed!" }
                0
            }
    }

    override fun getDrawable(r: Theme.Resource): Drawable? {
        if (r.type == Theme.Resource.TYPE_COLOR) {
            val color = getColor(r)
            return if (color != 0) color.toDrawable() else null
        } else if (r.type == Theme.Resource.TYPE_DRAWABLE) {
            return parent?.getDrawable(r)
        }
        return null
    }

    override fun getString(r: Theme.Resource, vararg args: Any): String? {
        return null
    }

    override val loggableTag: String by lazy { "Theme[$id]" }
}