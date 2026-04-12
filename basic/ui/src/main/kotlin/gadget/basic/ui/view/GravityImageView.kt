package gadget.basic.ui.view

import android.content.Context
import android.graphics.Canvas
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import gadget.basic.annotation.DslScope
import gadget.basic.exception.throws
import gadget.basic.ui.common.GravityX
import gadget.basic.ui.dsl.ViewScope
import gadget.basic.ui.dsl.marginLayoutParams
import gadget.basic.ui.dsl.scope

@DslScope
class GravityImageView(context: Context) : AppCompatImageView(context) {

    var gravity: Int = GravityX.N
        set(value) {
            val target = if (value == GravityX.L || value == GravityX.T || value == GravityX.R || value == GravityX.B) {
                value
            } else {
                GravityX.N
            }
            if (field != target) {
                field = target
                scaleType = if (target == GravityX.N) {
                    ScaleType.CENTER_INSIDE
                } else {
                    ScaleType.MATRIX
                }
                postInvalidate()
            }
        }

    override fun setScaleType(scaleType: ScaleType?) {
        if ((gravity == GravityX.N && scaleType != ScaleType.CENTER_INSIDE) ||
            (gravity != GravityX.N && scaleType != ScaleType.MATRIX)) {
            IllegalArgumentException("Not support $scaleType with gravity: $gravity!").throws()
        }
        super.setScaleType(scaleType)
    }

    override fun onDraw(canvas: Canvas) {
        val d = drawable
        if (gravity != GravityX.N && d != null && d.intrinsicWidth > 0 && d.intrinsicHeight > 0) {
            val m = imageMatrix
            if (gravity == GravityX.T || gravity == GravityX.B) {
                val s = width.toFloat() / d.intrinsicWidth.toFloat()
                m.setScale(s, s)
                m.postTranslate(0F, if (gravity == GravityX.T) 0F else height.toFloat() - d.intrinsicHeight.toFloat() * s)
            } else {
                val s = height.toFloat() / d.intrinsicHeight.toFloat()
                m.setScale(s, s)
                m.postTranslate(if (gravity == GravityX.L) 0F else width.toFloat() - d.intrinsicWidth.toFloat() * s, 0F)
            }
            imageMatrix = m
        }
        super.onDraw(canvas)
    }
}

inline fun <V : ViewGroup> ViewScope<V>.GravityImageView(
    params: (@DslScope GravityImageView).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<GravityImageView>).() -> Unit = {},
): GravityImageView = scope(GravityImageView(context), params, lambda)
