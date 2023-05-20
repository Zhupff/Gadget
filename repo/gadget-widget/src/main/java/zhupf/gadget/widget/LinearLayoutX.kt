package zhupf.gadget.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.LinearLayout
import zhupf.gadget.widget.attr.CornerClip
import zhupf.gadget.widget.attr.CornerClipAttr
import zhupf.gadget.widget.attr.ICornerClip
import zhupf.gadget.widget.attr.IInsetFit
import zhupf.gadget.widget.attr.InsetFit
import zhupf.gadget.widget.attr.InsetFitAttr

open class LinearLayoutX @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), ICornerClip, IInsetFit {

    override val cornerClip: CornerClip = CornerClip(this, CornerClipAttr(
        attrs, R.styleable.LinearLayoutX, R.styleable.LinearLayoutX_allCornerRadius,
        R.styleable.LinearLayoutX_tlCornerRadius, R.styleable.LinearLayoutX_tlCornerRadiusX, R.styleable.LinearLayoutX_tlCornerRadiusY,
        R.styleable.LinearLayoutX_trCornerRadius, R.styleable.LinearLayoutX_trCornerRadiusX, R.styleable.LinearLayoutX_trCornerRadiusY,
        R.styleable.LinearLayoutX_brCornerRadius, R.styleable.LinearLayoutX_brCornerRadiusX, R.styleable.LinearLayoutX_brCornerRadiusY,
        R.styleable.LinearLayoutX_blCornerRadius, R.styleable.LinearLayoutX_blCornerRadiusX, R.styleable.LinearLayoutX_blCornerRadiusY,
    )
    )

    override val insetFit: InsetFit = InsetFit(this, InsetFitAttr(
        attrs, R.styleable.LinearLayoutX,
        R.styleable.LinearLayoutX_fitSystemLeft, R.styleable.LinearLayoutX_fitSystemTop,
        R.styleable.LinearLayoutX_fitSystemRight, R.styleable.LinearLayoutX_fitSystemBottom
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