package gadget.basic.theme

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import gadget.basic.Gadget
import gadget.basic.exception.throws
import gadget.basic.log.Loggable
import gadget.basic.log.logW

abstract class NightTheme(
    val name: String,
) : Theme, Loggable by Loggable.Tag(true) {

    companion object : NightTheme("Default")

    override val id: String = "${name}-night"

    override fun getColor(r: Theme.Resource): Int = when (r.id) {
        gadget.basic.R.color.ThemePrimary    -> 0xFFADC6FF.toInt()
        gadget.basic.R.color.ThemeOnPrimary  -> 0xFF102F60.toInt()
        gadget.basic.R.color.ThemeBackground -> 0xFF0C0E13.toInt()
        gadget.basic.R.color.ThemeForeground -> 0xFFE2E2E9.toInt()
        gadget.basic.R.color.ThemeSurface    -> 0xFF1E1F25.toInt()
        gadget.basic.R.color.ThemeOutline    -> 0xFF8E9099.toInt()
        gadget.basic.R.color.ThemeError      -> 0xFFFFB4AB.toInt()
        gadget.basic.R.color.ThemeOnError    -> 0xFF690005.toInt()
        else -> {
            if (Gadget.debuggable) {
                IllegalStateException("getColor($r) failed!").throws()
            } else {
                logW(null) { "getColor($r) failed!" }
                0
            }
        }
    }

    override fun getDrawable(r: Theme.Resource): Drawable? {
        val color = getColor(r)
        return if (color != 0) color.toDrawable() else null
    }

    override fun getString(r: Theme.Resource, vararg args: Any): String? {
        return null
    }
}