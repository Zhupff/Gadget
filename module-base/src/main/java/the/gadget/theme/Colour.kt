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

    Surface, OnSurface,
    SurfaceVariant, OnSurfaceVariant,

    Outline, Shadow,

    InverseSurface, InverseOnSurface, InversePrimary,
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
    val surface: Int, val onSurface: Int,
    val surfaceVariant: Int, val onSurfaceVariant: Int,
    val outline: Int, val shadow: Int,
    val inverseSurface: Int, val inverseOnSurface: Int, val inversePrimary: Int,
) {
    enum class Mode() {
        Light, Dark,
        ;
        fun isLightMode(): Boolean = this == Light
        fun isDarkMode(): Boolean = this == Dark
    }
    fun apply() {
        Colour.Origin.color               = this.originArgb
        Colour.Primary.color              = this.primary
        Colour.OnPrimary.color            = this.onPrimary
        Colour.PrimaryContainer.color     = this.primaryContainer
        Colour.OnPrimaryContainer.color   = this.onPrimaryContainer
        Colour.Secondary.color            = this.secondary
        Colour.OnSecondary.color          = this.onSecondary
        Colour.SecondaryContainer.color   = this.secondaryContainer
        Colour.OnSecondaryContainer.color = this.onSecondaryContainer
        Colour.Tertiary.color             = this.tertiary
        Colour.OnTertiary.color           = this.onTertiary
        Colour.TertiaryContainer.color    = this.tertiaryContainer
        Colour.OnTertiaryContainer.color  = this.onTertiaryContainer
        Colour.Error.color                = this.error
        Colour.OnError.color              = this.onError
        Colour.ErrorContainer.color       = this.errorContainer
        Colour.OnErrorContainer.color     = this.onErrorContainer
        Colour.Background.color           = this.background
        Colour.OnBackground.color         = this.onBackground
        Colour.Surface.color              = this.surface
        Colour.OnSurface.color            = this.onSurface
        Colour.SurfaceVariant.color       = this.surfaceVariant
        Colour.OnSurfaceVariant.color     = this.onSurfaceVariant
        Colour.Outline.color              = this.outline
        Colour.Shadow.color               = this.shadow
        Colour.InverseSurface.color       = this.inverseSurface
        Colour.InverseOnSurface.color     = this.inverseOnSurface
        Colour.InversePrimary.color       = this.inversePrimary
    }
}