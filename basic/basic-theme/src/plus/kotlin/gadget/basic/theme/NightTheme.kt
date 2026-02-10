package gadget.basic.theme

internal abstract class NightTheme(
    name: String,
    parent: NightTheme? = null,
) : AbstractTheme<NightTheme>(name, parent) {

    companion object : NightTheme("Default")

    final override val id: String = "${name}-Night"

    override val primaryColor   : Int = 0xFFADC6FF.toInt()
    override val onPrimaryColor : Int = 0xFF102F60.toInt()
    override val backgroundColor: Int = 0xFF0C0E13.toInt()
    override val foregroundColor: Int = 0xFFE2E2E9.toInt()
    override val surfaceColor   : Int = 0xFF1E1F25.toInt()
    override val outlineColor   : Int = 0xFF8E9099.toInt()
    override val errorColor     : Int = 0xFFFFB4AB.toInt()
    override val onErrorColor   : Int = 0xFF690005.toInt()
}