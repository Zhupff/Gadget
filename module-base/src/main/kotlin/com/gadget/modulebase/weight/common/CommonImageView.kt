package com.gadget.modulebase.weight.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.gadget.modulebase.R

open class CommonImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var allCornerRadius: Float = 0F
        protected set

    var tlCornerRadius: Float = 0F
        protected set

    var trCornerRadius: Float = 0F
        protected set

    var brCornerRadius: Float = 0F
        protected set

    var blCornerRadius: Float = 0F
        protected set

    protected val cornerRectF: RectF = RectF()

    protected val cornerPath: Path = Path()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CommonImageView).also {
            allCornerRadius = it.getDimension(R.styleable.CommonImageView_allCornerRadius, 0F)
            tlCornerRadius = it.getDimension(R.styleable.CommonImageView_tlCornerRadius, allCornerRadius)
            trCornerRadius = it.getDimension(R.styleable.CommonImageView_trCornerRadius, allCornerRadius)
            brCornerRadius = it.getDimension(R.styleable.CommonImageView_brCornerRadius, allCornerRadius)
            blCornerRadius = it.getDimension(R.styleable.CommonImageView_blCornerRadius, allCornerRadius)
        }.recycle()
    }

    override fun draw(canvas: Canvas?) {
        if (canvas == null) {
            super.draw(canvas)
            return
        }
        cornerRectF.set(0F, 0F, width.toFloat(), height.toFloat())
        cornerPath.reset()
        cornerPath.addRoundRect(cornerRectF, floatArrayOf(
            tlCornerRadius, tlCornerRadius, trCornerRadius, trCornerRadius,
            brCornerRadius, brCornerRadius, blCornerRadius, blCornerRadius
        ), Path.Direction.CW)
        canvas.save()
        canvas.clipPath(cornerPath)
        super.draw(canvas)
        canvas.restore()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (canvas == null) {
            super.dispatchDraw(canvas)
            return
        }
        cornerRectF.set(0F, 0F, width.toFloat(), height.toFloat())
        cornerPath.reset()
        cornerPath.addRoundRect(cornerRectF, floatArrayOf(
            tlCornerRadius, tlCornerRadius, trCornerRadius, trCornerRadius,
            brCornerRadius, brCornerRadius, blCornerRadius, blCornerRadius
        ), Path.Direction.CW)
        canvas.save()
        canvas.clipPath(cornerPath)
        super.dispatchDraw(canvas)
        canvas.restore()
    }

    fun setCornerRadius(tl: Float, tr: Float, br: Float, bl: Float) {
        tlCornerRadius = tl
        trCornerRadius = tr
        brCornerRadius = br
        blCornerRadius = bl
        invalidate()
    }
}