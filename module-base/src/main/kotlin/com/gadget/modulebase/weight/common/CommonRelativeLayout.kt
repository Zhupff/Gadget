package com.gadget.modulebase.weight.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.RelativeLayout
import com.gadget.modulebase.R

open class CommonRelativeLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

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

    var fitStatusBar: Boolean = false

    var fitNavigationBar: Boolean = false

    var statusBarHeight: Int = 0

    var navigationBarHeight: Int = 0

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CommonRelativeLayout).also {
            allCornerRadius = it.getDimension(R.styleable.CommonRelativeLayout_allCornerRadius, 0F)
            tlCornerRadius = it.getDimension(R.styleable.CommonRelativeLayout_tlCornerRadius, allCornerRadius)
            trCornerRadius = it.getDimension(R.styleable.CommonRelativeLayout_trCornerRadius, allCornerRadius)
            brCornerRadius = it.getDimension(R.styleable.CommonRelativeLayout_brCornerRadius, allCornerRadius)
            blCornerRadius = it.getDimension(R.styleable.CommonRelativeLayout_blCornerRadius, allCornerRadius)
            fitStatusBar = it.getBoolean(R.styleable.CommonRelativeLayout_fitStatusBar, false)
            fitNavigationBar = it.getBoolean(R.styleable.CommonRelativeLayout_fitNavigationBar, false)
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

    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        statusBarHeight = insets?.systemWindowInsetTop ?: 0
        navigationBarHeight = insets?.systemWindowInsetBottom ?: 0
        return super.onApplyWindowInsets(insets)
    }

    fun fitSystemBar(fitStatusBar: Boolean, fitNavigationBar: Boolean) {
        this.fitStatusBar = fitStatusBar
        this.fitNavigationBar = fitNavigationBar
        val targetPaddingTop = if (fitStatusBar) statusBarHeight else 0
        val targetPaddingBottom = if (fitNavigationBar) navigationBarHeight else 0
        if (paddingTop != targetPaddingTop || paddingBottom != targetPaddingBottom) {
            setPadding(0, targetPaddingTop, 0, targetPaddingBottom)
        }
    }
}