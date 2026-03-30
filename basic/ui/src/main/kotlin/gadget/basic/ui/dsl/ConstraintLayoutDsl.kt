package gadget.basic.ui.dsl

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import gadget.basic.annotation.DslScope

inline fun ConstraintLayout(
    context: Context,
    params: LayoutParamsDsl<*, ConstraintLayout>,
    lambda: (@DslScope ConstraintLayout).(ConstraintLayout) -> Unit = {},
): ConstraintLayout = ConstraintLayout(context).apply {
    params.init(this)
    lambda(this, this)
    ensureViewId()
}

inline fun <L : ViewGroup> L.ConstraintLayout(
    params: LayoutParamsDsl<*, ConstraintLayout>,
    lambda: (@DslScope ConstraintLayout).(ConstraintLayout) -> Unit = {},
): ConstraintLayout = ConstraintLayout(context).also {
    params.init(it)
    lambda(it, it)
    addView(it)
    it.ensureViewId()
}

class ConstraintLayoutParams<V : View>(
    size: Pair<Int, Int>,
    initializer: (@DslScope ConstraintLayout.LayoutParams).(V) -> Unit = {},
) : LayoutParamsDsl<ConstraintLayout.LayoutParams, V>(
    initializer, ConstraintLayout.LayoutParams(size.first, size.second),
)

fun <L : ConstraintLayout, V : View> L.LayoutParams(
    size: Pair<Int, Int>,
    initializer: (@DslScope ConstraintLayout.LayoutParams).(V) -> Unit = {},
): ConstraintLayoutParams<V> = ConstraintLayoutParams(size, initializer)

inline fun <V : View> V.constraintLayoutParams(
    lambda: (@DslScope ConstraintLayout.LayoutParams).(ConstraintLayout.LayoutParams) -> Unit,
): ConstraintLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? ConstraintLayout.LayoutParams ?: ConstraintLayout.LayoutParams(it)
    } ?: ConstraintLayout.LayoutParams(context, null)
    lambda(lp, lp)
    return lp
}

fun <P : ConstraintLayout.LayoutParams> P.leftToLeftOfParent() {
    leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
    leftToRight = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.rightToRightOfParent() {
    rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
    rightToLeft = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.topToTopOfParent() {
    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
    topToBottom = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.bottomToBottomOfParent() {
    bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
    bottomToTop = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.centerOfParentHorizontally() {
    leftToLeftOfParent()
    rightToRightOfParent()
}

fun <P : ConstraintLayout.LayoutParams> P.centerOfParentVertically() {
    topToTopOfParent()
    bottomToBottomOfParent()
}

fun <P : ConstraintLayout.LayoutParams> P.centerOfParent() {
    centerOfParentHorizontally()
    centerOfParentVertically()
}
