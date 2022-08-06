package the.gadget.weight.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.RelativeLayout

open class CommonRelativeLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), CornerClip, WindowFit {

    private val cornerClipView: CornerClipView = CornerClipView(this, context, attrs)
    private val windowFitViewGroup: WindowFitViewGroup = WindowFitViewGroup(this, context, attrs)

    override val tlCornerRadius: Float; get() = cornerClipView.tlCornerRadius
    override val trCornerRadius: Float; get() = cornerClipView.trCornerRadius
    override val blCornerRadius: Float; get() = cornerClipView.blCornerRadius
    override val brCornerRadius: Float; get() = cornerClipView.brCornerRadius

    override val fitStatusBar: Boolean;     get() = windowFitViewGroup.fitStatusBar
    override val fitNavigationBar: Boolean; get() = windowFitViewGroup.fitNavigationBar
    override val statusBarHeight: Int;      get() = windowFitViewGroup.statusBarHeight
    override val navigationBarHeight: Int;  get() = windowFitViewGroup.navigationBarHeight

    override fun draw(canvas: Canvas?) {
        cornerClipView.draw(canvas) { super.draw(canvas) }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        cornerClipView.dispatchDraw(canvas) { super.dispatchDraw(canvas) }
    }

    override fun updateCornerRadius(tl: Float, tr: Float, bl: Float, br: Float) {
        cornerClipView.updateCornerRadius(tl, tr, bl, br)
    }

    override fun fitSystemBar(fitStatusBar: Boolean, fitNavigationBar: Boolean) {
        windowFitViewGroup.fitSystemBar(fitStatusBar, fitNavigationBar)
    }
}