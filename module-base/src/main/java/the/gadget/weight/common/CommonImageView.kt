package the.gadget.weight.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import the.gadget.module.base.R

open class CommonImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var allCornerRadius: Float = 0F
        protected set

    var tlCornerRadius: Float = 0F
        protected set

    var trCornerRadius: Float = 0F
        protected set

    var blCornerRadius: Float = 0F
        protected set

    var brCornerRadius: Float = 0F
        protected set

    private val innerPath: Path = Path()

    private val outerPath: Path = Path()

    private val outlineRect: RectF = RectF()

    private val outlinePaint: Paint = Paint().also {
        it.isAntiAlias = true
        it.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CommonImageView).also {
            allCornerRadius = it.getDimension(R.styleable.CommonImageView_allCornerRadius, 0F)
            tlCornerRadius = it.getDimension(R.styleable.CommonImageView_tlCornerRadius, allCornerRadius)
            trCornerRadius = it.getDimension(R.styleable.CommonImageView_trCornerRadius, allCornerRadius)
            blCornerRadius = it.getDimension(R.styleable.CommonImageView_blCornerRadius, allCornerRadius)
            brCornerRadius = it.getDimension(R.styleable.CommonImageView_brCornerRadius, allCornerRadius)
        }.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        outlineRect.set(0f, 0f, w.toFloat(), h.toFloat())
        innerPath.reset()
        innerPath.addRoundRect(outlineRect, floatArrayOf(
            tlCornerRadius, tlCornerRadius, trCornerRadius, trCornerRadius,
            brCornerRadius, brCornerRadius, blCornerRadius, blCornerRadius
        ), Path.Direction.CW)
        outerPath.reset()
        outerPath.addRect(outlineRect, Path.Direction.CW)
        outerPath.op(innerPath, Path.Op.DIFFERENCE)
    }

    override fun draw(canvas: Canvas?) {
        if (canvas != null && hasCorner()) {
            val sc = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
            super.draw(canvas)
            canvas.drawPath(outerPath, outlinePaint)
            canvas.restoreToCount(sc)
        } else {
            super.draw(canvas)
        }
    }

    open fun setCornerRadius(tl: Float, tr: Float, bl: Float, br: Float) {
        tlCornerRadius = tl
        trCornerRadius = tr
        blCornerRadius = bl
        brCornerRadius = br
        postInvalidate()
    }

    protected fun hasCorner(): Boolean = tlCornerRadius > 0F || trCornerRadius > 0F || brCornerRadius > 0F || blCornerRadius > 0F
}