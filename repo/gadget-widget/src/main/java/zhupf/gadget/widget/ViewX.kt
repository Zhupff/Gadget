package zhupf.gadget.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

open class ViewX @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ICornerClip {

    override val cornerClip: CornerClip = CornerClip(this, CornerClipAttr(
        attrs, R.styleable.ViewX, R.styleable.ViewX_allCornerRadius,
        R.styleable.ViewX_tlCornerRadius, R.styleable.ViewX_tlCornerRadiusX, R.styleable.ViewX_tlCornerRadiusY,
        R.styleable.ViewX_trCornerRadius, R.styleable.ViewX_trCornerRadiusX, R.styleable.ViewX_trCornerRadiusY,
        R.styleable.ViewX_brCornerRadius, R.styleable.ViewX_brCornerRadiusX, R.styleable.ViewX_brCornerRadiusY,
        R.styleable.ViewX_blCornerRadius, R.styleable.ViewX_blCornerRadiusX, R.styleable.ViewX_blCornerRadiusY,
    ))

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