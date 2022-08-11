package the.gadget.weight.common

interface CornerClip {

    val tlCornerRadius: Float

    val trCornerRadius: Float

    val blCornerRadius: Float

    val brCornerRadius: Float

    fun setCornerRadius(radius: Float)

    fun setCornerRadius(tl: Float, tr: Float, bl: Float, br: Float)
}