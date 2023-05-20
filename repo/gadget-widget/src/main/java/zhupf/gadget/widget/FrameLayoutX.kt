package zhupf.gadget.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout

open class FrameLayoutX @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ICornerClip, IInsetFit {

    override val cornerClip: CornerClip = CornerClip(this, CornerClipAttr(
        attrs, R.styleable.FrameLayoutX, R.styleable.FrameLayoutX_allCornerRadius,
        R.styleable.FrameLayoutX_tlCornerRadius, R.styleable.FrameLayoutX_tlCornerRadiusX, R.styleable.FrameLayoutX_tlCornerRadiusY,
        R.styleable.FrameLayoutX_trCornerRadius, R.styleable.FrameLayoutX_trCornerRadiusX, R.styleable.FrameLayoutX_trCornerRadiusY,
        R.styleable.FrameLayoutX_brCornerRadius, R.styleable.FrameLayoutX_brCornerRadiusX, R.styleable.FrameLayoutX_brCornerRadiusY,
        R.styleable.FrameLayoutX_blCornerRadius, R.styleable.FrameLayoutX_blCornerRadiusX, R.styleable.FrameLayoutX_blCornerRadiusY,
    ))

    override val insetFit: InsetFit = InsetFit(this, InsetFitAttr(
        attrs, R.styleable.FrameLayoutX,
        R.styleable.FrameLayoutX_fitSystemLeft, R.styleable.FrameLayoutX_fitSystemTop,
        R.styleable.FrameLayoutX_fitSystemRight, R.styleable.FrameLayoutX_fitSystemBottom
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

    override fun dispatchDraw(canvas: Canvas?) {
        if (canvas == null || !cornerClip.needToClip) {
            super.dispatchDraw(canvas)
            return
        }
        canvas.save()
        cornerClip.clip(canvas)
        super.dispatchDraw(canvas)
        canvas.restore()
    }

    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        if (insets != null) insetFit.fit(insets)
        return super.onApplyWindowInsets(insets)
    }
}