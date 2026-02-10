package gadget.basic.theme

internal abstract class LightTheme(
    name: String,
    parent: LightTheme? = null,
) : AbstractTheme<LightTheme>(name, parent) {

    companion object : LightTheme("Default")

    final override val id: String = "${name}-Light"

    override val primaryColor   : Int = 0xFF445E91.toInt()
    override val onPrimaryColor : Int = 0xFFFFFFFF.toInt()
    override val backgroundColor: Int = 0xFFFFFFFF.toInt()
    override val foregroundColor: Int = 0xFF1A1B20.toInt()
    override val surfaceColor   : Int = 0xFFEDEDF4.toInt()
    override val outlineColor   : Int = 0xFF74777F.toInt()
    override val errorColor     : Int = 0xFFBA1A1A.toInt()
    override val onErrorColor   : Int = 0xFFFFFFFF.toInt()
}