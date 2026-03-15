package gadget.basic.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.withStyledAttributes
import gadget.basic.exception.throws
import gadget.basic.ui.common.SideGravity

class FitSideImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var fitSide: Int = SideGravity.T
        set(value) {
            if (value != SideGravity.L && value != SideGravity.T && value != SideGravity.R && value != SideGravity.B) {
                throw IllegalArgumentException("Not support ${value}!")
            }
            if (field != value) {
                field = value
                postInvalidate()
            }
        }

    init {
        context.withStyledAttributes(attrs, gadget.basic.ui.R.styleable.FitSideImageView) {
            fitSide = getInt(gadget.basic.ui.R.styleable.FitSideImageView_FitSide, SideGravity.T)
        }
        scaleType = ScaleType.MATRIX
    }

    override fun setScaleType(scaleType: ScaleType?) {
        if (scaleType != ScaleType.MATRIX) {
            IllegalArgumentException("Not support ${scaleType}!").throws()
        }
        super.setScaleType(scaleType)
    }

    override fun onDraw(canvas: Canvas) {
        drawable?.let {
            val m = imageMatrix
            if (fitSide == SideGravity.T || fitSide == SideGravity.B) {
                val s = width.toFloat() / it.intrinsicWidth.toFloat()
                m.setScale(s, s)
                m.postTranslate(0F, if (fitSide == SideGravity.T) 0F else height.toFloat() - it.intrinsicHeight.toFloat() * s)
            } else {
                val s = height.toFloat() / it.intrinsicHeight.toFloat()
                m.setScale(s, s)
                m.postTranslate(if (fitSide == SideGravity.L) 0F else width.toFloat() - it.intrinsicWidth.toFloat() * s, 0F)
            }
            imageMatrix = m
        }
        super.onDraw(canvas)
    }
}