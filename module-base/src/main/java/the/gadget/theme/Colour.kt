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