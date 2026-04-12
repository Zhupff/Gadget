package gadget.basic.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.view.View
import android.view.ViewGroup
import gadget.basic.annotation.DslScope
import gadget.basic.ui.dsl.ViewScope
import gadget.basic.ui.dsl.marginLayoutParams
import gadget.basic.ui.dsl.scope

@DslScope
class GradientTransparentL2R(context: Context) : View(context) {

    companion object {
        private val MASK_COLORS = intArrayOf(Color.WHITE, Color.TRANSPARENT)
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader = null
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 || h > 0) {
            paint.shader = LinearGradient(0F, 0F, w.toFloat(), 0F, MASK_COLORS, null, Shader.TileMode.CLAMP)
        }
    }

    override fun draw(canvas: Canvas) {
        if (background != null && width > 0 && height > 0 && paint.shader != null) {
            val count = canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null)
            super.draw(canvas)
            canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
            canvas.restoreToCount(count)
        } else {
            super.draw(canvas)
        }
    }
}

inline fun <V : ViewGroup> ViewScope<V>.GradientTransparentL2R(
    params: (@DslScope GradientTransparentL2R).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<GradientTransparentL2R>).() -> Unit = {},
): GradientTransparentL2R = scope(GradientTransparentL2R(context), params, lambda)
