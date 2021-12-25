package the.gadget.modulebase.weight.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout
import the.gadget.modulebase.R

open class CommonFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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

    protected val cornerRect: RectF = RectF()

    protected val cornerPath: Path = Path()

    var fitStatusBar: Boolean = false
        protected set

    var fitNavigationBar: Boolean = false
        protected set

    var statusBarHeight: Int = 0
        protected set

    var navigationBarHeight: Int = 0
        protected set

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CommonFrameLayout).also {
            allCornerRadius = it.getDimension(R.styleable.CommonFrameLayout_allCornerRadius, 0F)
            tlCornerRadius = it.getDimension(R.styleable.CommonFrameLayout_tlCornerRadius, allCornerRadius)
            trCornerRadius = it.getDimension(R.styleable.CommonFrameLayout_trCornerRadius, allCornerRadius)
            blCornerRadius = it.getDimension(R.styleable.CommonFrameLayout_blCornerRadius, allCornerRadius)
            brCornerRadius = it.getDimension(R.styleable.CommonFrameLayout_brCornerRadius, allCornerRadius)
            fitStatusBar = it.getBoolean(R.styleable.CommonFrameLayout_fitStatusBar, false)
            fitNavigationBar = it.getBoolean(R.styleable.CommonFrameLayout_fitNavigationBar, false)
        }.recycle()
    }

    override fun draw(canvas: Canvas?) {
        if (canvas == null) {
            super.draw(canvas)
            return
        }
        cornerRect.set(0F, 0F, width.toFloat(), height.toFloat())
        cornerPath.reset()
        cornerPath.addRoundRect(cornerRect, floatArrayOf(
            tlCornerRadius, tlCornerRadius, trCornerRadius, trCornerRadius,
            brCornerRadius, brCornerRadius, blCornerRadius, blCornerRadius
        ), Path.Direction.CW)
        canvas.save()
        canvas.clipPath(cornerPath)
        super.draw(canvas)
        canvas.restore()
    }

    fun setCornerRadius(tl: Float, tr: Float, bl: Float, br: Float) {
        tlCornerRadius = tl
        trCornerRadius = tr
        blCornerRadius = bl
        brCornerRadius = br
        invalidate()
    }

    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        statusBarHeight = insets?.systemWindowInsetTop ?: 0
        navigationBarHeight = insets?.systemWindowInsetBottom ?: 0
        fitSystemBar(fitStatusBar, fitNavigationBar)
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