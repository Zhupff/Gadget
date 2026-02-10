package gadget.basic.theme

import com.google.mcu.hct.Hct
import com.google.mcu.scheme.SchemeTonalSpot
import gadget.basic.Gadget
import gadget.basic.exception.throws
import gadget.basic.log.logW

internal open class NightTheme(
    name: String,
    parent: NightTheme? = null,
) : AbstractTheme<NightTheme>(name, parent) {

    companion object : NightTheme("Default") {
        override var light: LightTheme? = null
            get() = LightTheme
    }

    final override val id: String = "${name}-Night"

    override var primaryColor   : Int = 0xFFADC6FF.toInt()
        protected set
    override var onPrimaryColor : Int = 0xFF102F60.toInt()
        protected set
    override var backgroundColor: Int = 0xFF0C0E13.toInt()
        protected set
    override var foregroundColor: Int = 0xFFE2E2E9.toInt()
        protected set
    override var surfaceColor   : Int = 0xFF1E1F25.toInt()
        protected set
    override var outlineColor   : Int = 0xFF8E9099.toInt()
        protected set
    override var errorColor     : Int = 0xFFFFB4AB.toInt()
        protected set
    override var onErrorColor   : Int = 0xFF690005.toInt()
        protected set

    open var light: LightTheme? = null
        protected set

    fun create(name: String, argb: Int, light: LightTheme?): NightTheme {
        if (name == this.name) {
            if (Gadget.debuggable) {
                IllegalArgumentException("The theme with the same name is already exists!").throws()
            } else {
                logW(null) { "The theme with the same name is already exists!" }
                return NightTheme
            }
        }
        if (argb == 0) {
            if (Gadget.debuggable) {
                IllegalArgumentException("argb should not be 0!").throws()
            } else {
                logW(null) { "argb should not be 0!" }
                return NightTheme
            }
        }
        val scheme = SchemeTonalSpot(Hct.fromInt(argb), true, 0.0)
        val theme = NightTheme(name, this).also { theme ->
            theme.primaryColor = scheme.primary
            theme.onPrimaryColor = scheme.onPrimary
            theme.backgroundColor = scheme.surfaceContainerLowest
            theme.foregroundColor = scheme.onSurface
            theme.surfaceColor = scheme.surfaceContainer
            theme.outlineColor = scheme.outline
            theme.errorColor = scheme.error
            theme.onErrorColor = scheme.onError
        }
        if (light != null) {
            theme.light = light
        } else {
            theme.light = this.light?.create(name, argb, theme)
        }
        return theme
    }
}