package the.gadget.modulebase.weight.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import the.gadget.modulebase.R

open class CommonLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


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

    var fitStatusBar: Boolean = false; protected set

    var fitNavigationBar: Boolean = false; protected set

    var statusBarHeight: Int = 0; protected set

    var navigationBarHeight: Int = 0; protected set

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CommonLinearLayout).also {
            allCornerRadius = it.getDimension(R.styleable.CommonLinearLayout_allCornerRadius, 0F)
            tlCornerRadius = it.getDimension(R.styleable.CommonLinearLayout_tlCornerRadius, allCornerRadius)
            trCornerRadius = it.getDimension(R.styleable.CommonLinearLayout_trCornerRadius, allCornerRadius)
            blCornerRadius = it.getDimension(R.styleable.CommonLinearLayout_blCornerRadius, allCornerRadius)
            brCornerRadius = it.getDimension(R.styleable.CommonLinearLayout_brCornerRadius, allCornerRadius)
            fitStatusBar = it.getBoolean(R.styleable.CommonLinearLayout_fitStatusBar, false)
            fitNavigationBar = it.getBoolean(R.styleable.CommonLinearLayout_fitNavigationBar, false)
        }.recycle()
    }

    override fun draw(canvas: Canvas?) {
        if (canvas != null && hasCorner()) {
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
        } else {
            super.draw(canvas)
        }
    }

    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        val compatInsets = ViewCompat.getRootWindowInsets(this)?.getInsets(WindowInsetsCompat.Type.systemBars())
        statusBarHeight = compatInsets?.top ?: 0
        navigationBarHeight = compatInsets?.bottom ?: 0
        fitSystemBar(fitStatusBar, fitNavigationBar)
        return super.onApplyWindowInsets(insets)
    }

    fun setCornerRadius(tl: Float, tr: Float, bl: Float, br: Float) {
        tlCornerRadius = tl
        trCornerRadius = tr
        blCornerRadius = bl
        brCornerRadius = br
        invalidate()
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

    private fun hasCorner(): Boolean = tlCornerRadius > 0F || trCornerRadius > 0F || brCornerRadius > 0F || blCornerRadius > 0F
}