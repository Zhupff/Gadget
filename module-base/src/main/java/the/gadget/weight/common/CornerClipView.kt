package the.gadget.weight.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import the.gadget.module.base.R

class CornerClipView(
    private val view: View,
    context: Context,
    attrs: AttributeSet? = null
) {

    var tlCornerRadius: Float = 0F; private set
    var trCornerRadius: Float = 0F; private set
    var blCornerRadius: Float = 0F; private set
    var brCornerRadius: Float = 0F; private set

    private val innerPath: Path = Path()
    private val outerPath: Path = Path()
    private val outlineRect: RectF = RectF()
    private val outlinePaint: Paint = Paint().also {
        it.isAntiAlias = true
        it.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }

    private val width: Float; get() = view.width.toFloat()
    private val height: Float; get() = view.height.toFloat()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CornerClipView).also {
            val allCornerRadius = it.getDimension(R.styleable.CornerClipView_allCornerRadius, 0F)
            tlCornerRadius = it.getDimension(R.styleable.CornerClipView_tlCornerRadius, allCornerRadius)
            trCornerRadius = it.getDimension(R.styleable.CornerClipView_trCornerRadius, allCornerRadius)
            blCornerRadius = it.getDimension(R.styleable.CornerClipView_blCornerRadius, allCornerRadius)
            brCornerRadius = it.getDimension(R.styleable.CornerClipView_brCornerRadius, allCornerRadius)
        }.recycle()
        view.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> adjustCorner() }
    }

    fun clip(canvas: Canvas?, draw: () -> Unit) {
        if (canvas == null || !hasCorner()) {
            draw.invoke()
            return
        }
        val count = canvas.saveLayer(0f, 0f, width, height, null)
        draw.invoke()
        canvas.drawPath(outerPath, outlinePaint)
        canvas.restoreToCount(count)
    }

    fun setCornerRadius(tl: Float, tr: Float, bl: Float, br: Float) {
        if (tlCornerRadius != tl || trCornerRadius != tr || blCornerRadius != bl || brCornerRadius != br) {
            tlCornerRadius = tl
            trCornerRadius = tr
            blCornerRadius = bl
            brCornerRadius = br
            adjustCorner()
            view.postInvalidate()
        }
    }

    private fun hasCorner(): Boolean = tlCornerRadius > 0F || trCornerRadius > 0F || brCornerRadius > 0F || blCornerRadius > 0F

    private fun adjustCorner() {
        outlineRect.set(0f, 0f, width, height)
        innerPath.reset()
        outerPath.reset()
        if (hasCorner()) {
            innerPath.addRoundRect(
                outlineRect, floatArrayOf(
                    tlCornerRadius, tlCornerRadius, trCornerRadius, trCornerRadius,
                    brCornerRadius, brCornerRadius, blCornerRadius, blCornerRadius
                ), Path.Direction.CW
            )
            outerPath.addRect(outlineRect, Path.Direction.CW)
            outerPath.op(innerPath, Path.Op.DIFFERENCE)
        }
    }
}