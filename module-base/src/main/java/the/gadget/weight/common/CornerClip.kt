package the.gadget.weight.common

interface CornerClip {

    val tlCornerRadius: Float

    val trCornerRadius: Float

    val blCornerRadius: Float

    val brCornerRadius: Float

    fun updateCornerRadius(tl: Float, tr: Float, bl: Float, br: Float)
}