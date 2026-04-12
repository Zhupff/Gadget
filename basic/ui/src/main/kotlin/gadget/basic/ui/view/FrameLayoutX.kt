package gadget.basic.ui.view

import android.content.Context
import android.graphics.Outline
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.annotation.UiThread
import androidx.core.view.WindowInsetsCompat
import gadget.basic.Gadget
import gadget.basic.annotation.DslScope
import gadget.basic.exception.throws
import gadget.basic.ui.common.GravityX
import gadget.basic.ui.dsl.ViewScope
import gadget.basic.ui.dsl.marginLayoutParams
import kotlin.math.min

@DslScope
class FrameLayoutX(context: Context) : FrameLayout(context) {

    var fitWindowInsetGravity: Int = GravityX.N
        @UiThread
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

    var clipCornerGravity: Int = GravityX.N
        @UiThread
        set(value) {
            var target = GravityX.N
            if (value and GravityX.A == GravityX.A) {
                target = GravityX.A
            } else if (value and GravityX.L == GravityX.L) {
                target = GravityX.L
            } else if (value and GravityX.T == GravityX.T) {
                target = GravityX.T
            } else if (value and GravityX.R == GravityX.R) {
                target = GravityX.R
            } else if (value and GravityX.B == GravityX.B) {
                target = GravityX.B
            } else if (value and GravityX.TL == GravityX.TL) {
                target = GravityX.TL
            } else if (value and GravityX.TR == GravityX.TR) {
                target = GravityX.TR
            } else if (value and GravityX.BR == GravityX.BR) {
                target = GravityX.BR
            } else if (value and GravityX.BL == GravityX.BL) {
                target = GravityX.BL
            } else {
                target = GravityX.N
            }
            if (target != value && Gadget.debuggable) {
                IllegalArgumentException("Illegal clipCornerGravity value: $value!").throws()
            }
            if (field != target) {
                field = target
                invalidateOutline()
            }
        }

    var clipCornerRadius: Float = 0F
        @UiThread
        set(value) {
            val target = if (value < 0F) -1F else value
            if (field != target) {
                field = target
                invalidateOutline()
            }
        }

    private val clipCornerRadiusArray = FloatArray(8)

    init {
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val diameter = min(width, height)
                val r = if (clipCornerRadius < 0F) diameter / 2F else min(clipCornerRadius, diameter / 2F)
                clipCornerRadiusArray.fill(0F)
                println("@@@ ${clipCornerGravity} ${clipCornerRadius} ${r}")
                when (clipCornerGravity) {
                    GravityX.N -> {
                        outline.setRect(0, 0, width, height)
                    }
                    GravityX.L -> {
                        clipCornerRadiusArray[0] = r
                        clipCornerRadiusArray[1] = r
                        clipCornerRadiusArray[6] = r
                        clipCornerRadiusArray[7] = r
                        outline.setRoundRect(0, 0, width + r.toInt(), height, r)
                    }
                    GravityX.T -> {
                        clipCornerRadiusArray[0] = r
                        clipCornerRadiusArray[1] = r
                        clipCornerRadiusArray[2] = r
                        clipCornerRadiusArray[3] = r
                        outline.setRoundRect(0, 0, width, height + r.toInt(), r)
                    }
                    GravityX.R -> {
                        clipCornerRadiusArray[2] = r
                        clipCornerRadiusArray[3] = r
                        clipCornerRadiusArray[4] = r
                        clipCornerRadiusArray[5] = r
                        outline.setRoundRect(0 - r.toInt(), 0, width, height, r)
                    }
                    GravityX.B -> {
                        clipCornerRadiusArray[4] = r
                        clipCornerRadiusArray[5] = r
                        clipCornerRadiusArray[6] = r
                        clipCornerRadiusArray[7] = r
                        outline.setRoundRect(0, 0 - r.toInt(), width, height, r)
                    }
                    GravityX.TL -> {
                        clipCornerRadiusArray[0] = r
                        clipCornerRadiusArray[1] = r
                        outline.setRoundRect(0, 0, width + r.toInt(), height + r.toInt(), r)
                    }
                    GravityX.TR -> {
                        clipCornerRadiusArray[2] = r
                        clipCornerRadiusArray[3] = r
                        outline.setRoundRect(0 - r.toInt(), 0, width, height + r.toInt(), r)
                    }
                    GravityX.BR -> {
                        clipCornerRadiusArray[4] = r
                        clipCornerRadiusArray[5] = r
                        outline.setRoundRect(0 - r.toInt(), 0 - r.toInt(), width, height, r)
                    }
                    GravityX.BL -> {
                        clipCornerRadiusArray[6] = r
                        clipCornerRadiusArray[7] = r
                        outline.setRoundRect(0, 0 - r.toInt(), width + r.toInt(), height, r)
                    }
                    GravityX.A -> {
                        clipCornerRadiusArray.fill(r)
                        outline.setRoundRect(0, 0, width, height, r)
                    }
                }
            }
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

    override fun setClipToOutline(clipToOutline: Boolean) {
        if (!clipToOutline) {
            if (Gadget.debuggable) {
                IllegalArgumentException("Must support clipToOutline!").throws()
            } else {
                return
            }
        }
        super.setClipToOutline(true)
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
