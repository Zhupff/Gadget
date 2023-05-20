package zhupf.gadget.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import zhupf.gadget.widget.attr.CornerClip
import zhupf.gadget.widget.attr.CornerClipAttr
import zhupf.gadget.widget.attr.ICornerClip

open class EditTextX @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr), ICornerClip {

    override val cornerClip: CornerClip = CornerClip(this, CornerClipAttr(
        attrs, R.styleable.EditTextX, R.styleable.EditTextX_allCornerRadius,
        R.styleable.EditTextX_tlCornerRadius, R.styleable.EditTextX_tlCornerRadiusX, R.styleable.EditTextX_tlCornerRadiusY,
        R.styleable.EditTextX_trCornerRadius, R.styleable.EditTextX_trCornerRadiusX, R.styleable.EditTextX_trCornerRadiusY,
        R.styleable.EditTextX_brCornerRadius, R.styleable.EditTextX_brCornerRadiusX, R.styleable.EditTextX_brCornerRadiusY,
        R.styleable.EditTextX_blCornerRadius, R.styleable.EditTextX_blCornerRadiusX, R.styleable.EditTextX_blCornerRadiusY,
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