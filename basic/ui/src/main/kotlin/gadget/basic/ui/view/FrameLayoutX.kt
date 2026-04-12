package gadget.basic.ui.view

import android.content.Context
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.core.view.WindowInsetsCompat
import gadget.basic.annotation.DslScope
import gadget.basic.ui.common.GravityX
import gadget.basic.ui.dsl.ViewScope
import gadget.basic.ui.dsl.marginLayoutParams

@DslScope
class FrameLayoutX(context: Context) : FrameLayout(context) {

    var fitWindowInsetGravity: Int = GravityX.A
        set(value) {
            var target = GravityX.N
            if (value and GravityX.A == GravityX.A) {
                target = GravityX.A
            } else {
                if (value and GravityX.L == GravityX.L) {
                    target = target or GravityX.L
                }
                if (value and GravityX.T == GravityX.T) {
                    target = target or GravityX.T
                }
                if (value and GravityX.R == GravityX.R) {
                    target = target or GravityX.R
                }
                if (value and GravityX.B == GravityX.B) {
                    target = target or GravityX.B
                }
            }
            if (field != target) {
                field = target
                applyWindowInsets()
            }
        }

    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets? {
        return super.onApplyWindowInsets(insets).also {
            applyWindowInsets()
        }
    }

    private fun applyWindowInsets() {
        if (rootWindowInsets == null) {
            return
        }
        val windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets, this)
        val systemBarInsets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
        val targetPaddingLeft   = if (fitWindowInsetGravity and GravityX.L == GravityX.L) systemBarInsets.left   else 0
        val targetPaddingTop    = if (fitWindowInsetGravity and GravityX.T == GravityX.T) systemBarInsets.top    else 0
        val targetPaddingRight  = if (fitWindowInsetGravity and GravityX.R == GravityX.R) systemBarInsets.right  else 0
        val targetPaddingBottom = if (fitWindowInsetGravity and GravityX.B == GravityX.B) systemBarInsets.bottom else 0
        if (paddingLeft   != targetPaddingLeft  ||
            paddingTop    != targetPaddingTop   ||
            paddingRight  != targetPaddingRight ||
            paddingBottom != targetPaddingBottom) {
            setPadding(targetPaddingLeft, targetPaddingTop, targetPaddingRight, targetPaddingBottom)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        applyWindowInsets()
    }
}

inline fun <V : ViewGroup> ViewScope<V>.FrameLayoutX(
    params: (@DslScope FrameLayoutX).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<FrameLayoutX>).() -> Unit = {},
): FrameLayoutX = FrameLayoutX(get()!!.context).also {
    get()!!.addView(it, params(it))
    lambda(ViewScope.get(it))
}
