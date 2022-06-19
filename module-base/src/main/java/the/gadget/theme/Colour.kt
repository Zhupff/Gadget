package the.gadget.theme

enum class Colour {
    Origin,

    Primary, OnPrimary,
    PrimaryContainer, OnPrimaryContainer,

    Secondary, OnSecondary,
    SecondaryContainer, OnSecondaryContainer,

    Tertiary, OnTertiary,
    TertiaryContainer, OnTertiaryContainer,

    Error, OnError,
    ErrorContainer, OnErrorContainer,

    Background, OnBackground,
    BackgroundContainer, OnBackgroundContainer,

    Surface, Outline, Shadow,

    InversePrimary, InverseSecondary, InverseTertiary,
    ;

    var color: Int = 0; internal set
}

data class Palette(
    val mode: Mode, val originArgb: Int,
    val primary: Int, val onPrimary: Int,
    val primaryContainer: Int, val onPrimaryContainer: Int,
    val secondary: Int, val onSecondary: Int,
    val secondaryContainer: Int, val onSecondaryContainer: Int,
    val tertiary: Int, val onTertiary: Int,
    val tertiaryContainer: Int, val onTertiaryContainer: Int,
    val error: Int, val onError: Int,
    val errorContainer: Int, val onErrorContainer: Int,
    val background: Int, val onBackground: Int,
    val backgroundContainer: Int, val onBackgroundContainer: Int,
    val surface: Int, val outline: Int, val shadow: Int,
    val inversePrimary: Int, val inverseSecondary: Int, val inverseTertiary: Int,
) {
    enum class Mode {
        Light, Dark,
        ;
        fun isLightMode(): Boolean = this == Light
        fun isDarkMode(): Boolean = this == Dark
    }

    fun apply() {
        Colour.Origin.color                = this.originArgb
        Colour.Primary.color               = this.primary
        Colour.OnPrimary.color             = this.onPrimary
        Colour.PrimaryContainer.color      = this.primaryContainer
        Colour.OnPrimaryContainer.color    = this.onPrimaryContainer
        Colour.Secondary.color             = this.secondary
        Colour.OnSecondary.color           = this.onSecondary
        Colour.SecondaryContainer.color    = this.secondaryContainer
        Colour.OnSecondaryContainer.color  = this.onSecondaryContainer
        Colour.Tertiary.color              = this.tertiary
        Colour.OnTertiary.color            = this.onTertiary
        Colour.TertiaryContainer.color     = this.tertiaryContainer
        Colour.OnTertiaryContainer.color   = this.onTertiaryContainer
        Colour.Error.color                 = this.error
        Colour.OnError.color               = this.onError
        Colour.ErrorContainer.color        = this.errorContainer
        Colour.OnErrorContainer.color      = this.onErrorContainer
        Colour.Background.color            = this.background
        Colour.OnBackground.color          = this.onBackground
        Colour.BackgroundContainer.color   = this.backgroundContainer
        Colour.OnBackgroundContainer.color = this.onBackgroundContainer
        Colour.Surface.color               = this.surface
        Colour.Outline.color               = this.outline
        Colour.Shadow.color                = this.shadow
        Colour.InversePrimary.color        = this.inversePrimary
        Colour.InverseSecondary.color      = this.inverseSecondary
        Colour.InverseTertiary.color       = this.inverseTertiary
    }
}