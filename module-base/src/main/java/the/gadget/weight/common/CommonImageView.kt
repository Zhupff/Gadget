package the.gadget.weight.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

open class CommonImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), CornerClip {

    private val cornerClipView: CornerClipView = CornerClipView(this, context, attrs)

    override val tlCornerRadius: Float; get() = cornerClipView.tlCornerRadius
    override val trCornerRadius: Float; get() = cornerClipView.trCornerRadius
    override val blCornerRadius: Float; get() = cornerClipView.blCornerRadius
    override val brCornerRadius: Float; get() = cornerClipView.brCornerRadius

    override fun draw(canvas: Canvas?) {
        cornerClipView.clip(canvas) { super.draw(canvas) }
    }

    override fun setCornerRadius(radius: Float) {
        setCornerRadius(radius, radius, radius, radius)
    }

    override fun setCornerRadius(tl: Float, tr: Float, bl: Float, br: Float) {
        cornerClipView.setCornerRadius(tl, tr, bl, br)
    }
}