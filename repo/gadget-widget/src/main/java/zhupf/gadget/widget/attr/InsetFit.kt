package zhupf.gadget.widget.attr

import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets
import androidx.annotation.StyleableRes
import androidx.core.view.WindowInsetsCompat

private const val L = 0
private const val T = 1
private const val R = 2
private const val B = 3

interface IInsetFit {
    val insetFit: InsetFit
}

class InsetFitAttr(
    val attrs: AttributeSet?,
    @StyleableRes val styleableId: IntArray,

    @StyleableRes val fitSystemLeftId: Int,
    @StyleableRes val fitSystemTopId: Int,
    @StyleableRes val fitSystemRightId: Int,
    @StyleableRes val fitSystemBottomId: Int,
)

class InsetFit(
    private val view: View,
    insetFitAttr: InsetFitAttr,
) {
    private val currentFits: BooleanArray = BooleanArray(4)
    private val pendingFits: BooleanArray = BooleanArray(4)
    private val fitLengths: IntArray = IntArray(4)

    init {
        view.context.obtainStyledAttributes(insetFitAttr.attrs, insetFitAttr.styleableId).also { attr ->
            currentFits[L] = attr.getBoolean(insetFitAttr.fitSystemLeftId, false)
            currentFits[T] = attr.getBoolean(insetFitAttr.fitSystemTopId, false)
            currentFits[R] = attr.getBoolean(insetFitAttr.fitSystemRightId, false)
            currentFits[B] = attr.getBoolean(insetFitAttr.fitSystemBottomId, false)
            currentFits.copyInto(pendingFits)
        }.recycle()
    }

    var fitSystemLeft: Boolean
        get() = currentFits[L]
        set(value) { pendingFits[L] = value }
    var fitSystemTop: Boolean
        get() = currentFits[T]
        set(value) { pendingFits[T] = value }
    var fitSystemRight: Boolean
        get() = currentFits[R]
        set(value) { pendingFits[R] = value }
    var fitSystemBottom: Boolean
        get() = currentFits[B]
        set(value) { pendingFits[B] = value }

    fun fit(insets: WindowInsets) {
        val compatInsets = WindowInsetsCompat.toWindowInsetsCompat(insets, view)
            .getInsets(WindowInsetsCompat.Type.systemBars())
        fitLengths[L] = compatInsets.left
        fitLengths[T] = compatInsets.top
        fitLengths[R] = compatInsets.right
        fitLengths[B] = compatInsets.bottom
        invalidate()
    }

    fun invalidate() {
        pendingFits.copyInto(currentFits)
        val targetPaddingLeft   = if (currentFits[L]) fitLengths[L] else 0
        val targetPaddingTop    = if (currentFits[T]) fitLengths[T] else 0
        val targetPaddingRight  = if (currentFits[R]) fitLengths[R] else 0
        val targetPaddingBottom = if (currentFits[B]) fitLengths[B] else 0
        if (view.paddingLeft   != targetPaddingLeft ||
            view.paddingTop    != targetPaddingTop ||
            view.paddingRight  != targetPaddingRight ||
            view.paddingBottom != targetPaddingBottom) {
            view.setPadding(targetPaddingLeft, targetPaddingTop, targetPaddingRight, targetPaddingBottom)
        }
    }
}