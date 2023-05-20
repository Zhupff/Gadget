package zhupf.gadget.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.RelativeLayout
import zhupf.gadget.widget.attr.CornerClip
import zhupf.gadget.widget.attr.CornerClipAttr
import zhupf.gadget.widget.attr.ICornerClip
import zhupf.gadget.widget.attr.IInsetFit
import zhupf.gadget.widget.attr.InsetFit
import zhupf.gadget.widget.attr.InsetFitAttr

open class RelativeLayoutX @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), ICornerClip, IInsetFit {

    override val cornerClip: CornerClip = CornerClip(this, CornerClipAttr(
        attrs, R.styleable.RelativeLayoutX, R.styleable.RelativeLayoutX_allCornerRadius,
        R.styleable.RelativeLayoutX_tlCornerRadius, R.styleable.RelativeLayoutX_tlCornerRadiusX, R.styleable.RelativeLayoutX_tlCornerRadiusY,
        R.styleable.RelativeLayoutX_trCornerRadius, R.styleable.RelativeLayoutX_trCornerRadiusX, R.styleable.RelativeLayoutX_trCornerRadiusY,
        R.styleable.RelativeLayoutX_brCornerRadius, R.styleable.RelativeLayoutX_brCornerRadiusX, R.styleable.RelativeLayoutX_brCornerRadiusY,
        R.styleable.RelativeLayoutX_blCornerRadius, R.styleable.RelativeLayoutX_blCornerRadiusX, R.styleable.RelativeLayoutX_blCornerRadiusY,
    )
    )

    override val insetFit: InsetFit = InsetFit(this, InsetFitAttr(
        attrs, R.styleable.RelativeLayoutX,
        R.styleable.RelativeLayoutX_fitSystemLeft, R.styleable.RelativeLayoutX_fitSystemTop,
        R.styleable.RelativeLayoutX_fitSystemRight, R.styleable.RelativeLayoutX_fitSystemBottom
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