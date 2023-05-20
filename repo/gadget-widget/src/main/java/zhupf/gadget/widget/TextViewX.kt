package zhupf.gadget.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import zhupf.gadget.widget.attr.CornerClip
import zhupf.gadget.widget.attr.CornerClipAttr
import zhupf.gadget.widget.attr.ICornerClip

open class TextViewX @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), ICornerClip {

    override val cornerClip: CornerClip = CornerClip(this, CornerClipAttr(
        attrs, R.styleable.TextViewX, R.styleable.TextViewX_allCornerRadius,
        R.styleable.TextViewX_tlCornerRadius, R.styleable.TextViewX_tlCornerRadiusX, R.styleable.TextViewX_tlCornerRadiusY,
        R.styleable.TextViewX_trCornerRadius, R.styleable.TextViewX_trCornerRadiusX, R.styleable.TextViewX_trCornerRadiusY,
        R.styleable.TextViewX_brCornerRadius, R.styleable.TextViewX_brCornerRadiusX, R.styleable.TextViewX_brCornerRadiusY,
        R.styleable.TextViewX_blCornerRadius, R.styleable.TextViewX_blCornerRadiusX, R.styleable.TextViewX_blCornerRadiusY,
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