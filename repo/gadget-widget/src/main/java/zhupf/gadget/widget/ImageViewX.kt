package zhupf.gadget.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import zhupf.gadget.widget.attr.CornerClip
import zhupf.gadget.widget.attr.CornerClipAttr
import zhupf.gadget.widget.attr.ICornerClip

open class ImageViewX @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), ICornerClip {

    override val cornerClip: CornerClip = CornerClip(this, CornerClipAttr(
        attrs, R.styleable.ImageViewX, R.styleable.ImageViewX_allCornerRadius,
        R.styleable.ImageViewX_tlCornerRadius, R.styleable.ImageViewX_tlCornerRadiusX, R.styleable.ImageViewX_tlCornerRadiusY,
        R.styleable.ImageViewX_trCornerRadius, R.styleable.ImageViewX_trCornerRadiusX, R.styleable.ImageViewX_trCornerRadiusY,
        R.styleable.ImageViewX_brCornerRadius, R.styleable.ImageViewX_brCornerRadiusX, R.styleable.ImageViewX_brCornerRadiusY,
        R.styleable.ImageViewX_blCornerRadius, R.styleable.ImageViewX_blCornerRadiusX, R.styleable.ImageViewX_blCornerRadiusY,
    )
    )

    override fun draw(canvas: Canvas?) {
        if (canvas == null || !cornerClip.needToClip) {
            super.draw(canvas)
            return
        }
        canvas.save()
        cornerClip.clip(canvas)
        super.draw(canvas)
        canvas.restore()
    }
}