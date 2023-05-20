package zhupf.gadget.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.recyclerview.widget.RecyclerView
import zhupf.gadget.widget.attr.CornerClip
import zhupf.gadget.widget.attr.CornerClipAttr
import zhupf.gadget.widget.attr.ICornerClip
import zhupf.gadget.widget.attr.IInsetFit
import zhupf.gadget.widget.attr.InsetFit
import zhupf.gadget.widget.attr.InsetFitAttr

open class RecyclerViewX @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), ICornerClip, IInsetFit {

    override val cornerClip: CornerClip = CornerClip(this, CornerClipAttr(
        attrs, R.styleable.RecyclerViewX, R.styleable.RecyclerViewX_allCornerRadius,
        R.styleable.RecyclerViewX_tlCornerRadius, R.styleable.RecyclerViewX_tlCornerRadiusX, R.styleable.RecyclerViewX_tlCornerRadiusY,
        R.styleable.RecyclerViewX_trCornerRadius, R.styleable.RecyclerViewX_trCornerRadiusX, R.styleable.RecyclerViewX_trCornerRadiusY,
        R.styleable.RecyclerViewX_brCornerRadius, R.styleable.RecyclerViewX_brCornerRadiusX, R.styleable.RecyclerViewX_brCornerRadiusY,
        R.styleable.RecyclerViewX_blCornerRadius, R.styleable.RecyclerViewX_blCornerRadiusX, R.styleable.RecyclerViewX_blCornerRadiusY,
    )
    )

    override val insetFit: InsetFit = InsetFit(this, InsetFitAttr(
        attrs, R.styleable.RecyclerViewX,
        R.styleable.RecyclerViewX_fitSystemLeft, R.styleable.RecyclerViewX_fitSystemTop,
        R.styleable.RecyclerViewX_fitSystemRight, R.styleable.RecyclerViewX_fitSystemBottom
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