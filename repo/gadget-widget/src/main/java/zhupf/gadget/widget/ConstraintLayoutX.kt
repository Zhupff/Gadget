package zhupf.gadget.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintLayout

open class ConstraintLayoutX @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICornerClip, IInsetFit {

    override val cornerClip: CornerClip = CornerClip(this, CornerClipAttr(
        attrs, R.styleable.ConstraintLayoutX, R.styleable.ConstraintLayoutX_allCornerRadius,
        R.styleable.ConstraintLayoutX_tlCornerRadius, R.styleable.ConstraintLayoutX_tlCornerRadiusX, R.styleable.ConstraintLayoutX_tlCornerRadiusY,
        R.styleable.ConstraintLayoutX_trCornerRadius, R.styleable.ConstraintLayoutX_trCornerRadiusX, R.styleable.ConstraintLayoutX_trCornerRadiusY,
        R.styleable.ConstraintLayoutX_brCornerRadius, R.styleable.ConstraintLayoutX_brCornerRadiusX, R.styleable.ConstraintLayoutX_brCornerRadiusY,
        R.styleable.ConstraintLayoutX_blCornerRadius, R.styleable.ConstraintLayoutX_blCornerRadiusX, R.styleable.ConstraintLayoutX_blCornerRadiusY,
    ))

    override val insetFit: InsetFit = InsetFit(this, InsetFitAttr(
        attrs, R.styleable.ConstraintLayoutX,
        R.styleable.ConstraintLayoutX_fitSystemLeft, R.styleable.ConstraintLayoutX_fitSystemTop,
        R.styleable.ConstraintLayoutX_fitSystemRight, R.styleable.ConstraintLayoutX_fitSystemBottom
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