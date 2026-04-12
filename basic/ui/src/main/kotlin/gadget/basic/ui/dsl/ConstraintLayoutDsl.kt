package gadget.basic.ui.dsl

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import gadget.basic.annotation.DslScope

inline fun ConstraintLayout(
    context: Context,
    params: (@DslScope ConstraintLayout).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<ConstraintLayout>).() -> Unit = {},
): ConstraintLayout = ConstraintLayout(context).also {
    params(it)
    lambda(ViewScope.get(it))
}

inline fun <V : ViewGroup> ViewScope<V>.ConstraintLayout(
    params: (@DslScope ConstraintLayout).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<ConstraintLayout>).() -> Unit = {},
): ConstraintLayout = scope(ConstraintLayout(context), params, lambda)

inline fun View.constraintLayoutParams(
    width: Int = layoutParams?.width ?: MATCH_CONSTRAINT,
    height: Int = layoutParams?.height ?: MATCH_CONSTRAINT,
    lambda: (ConstraintLayout.LayoutParams).() -> Unit = {},
): ConstraintLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? ConstraintLayout.LayoutParams ?: ConstraintLayout.LayoutParams(it)
    } ?: ConstraintLayout.LayoutParams(width, height)
    lambda(lp)
    if (this.layoutParams != null) {
        this.layoutParams = lp
    }
    return lp
}

fun ConstraintLayout.LayoutParams.unsetLeft() {
    leftToLeft = ConstraintLayout.LayoutParams.UNSET
    leftToRight = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.unsetRight() {
    rightToLeft = ConstraintLayout.LayoutParams.UNSET
    rightToRight = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.unsetTop() {
    topToTop = ConstraintLayout.LayoutParams.UNSET
    topToBottom = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.unsetBottom() {
    bottomToTop = ConstraintLayout.LayoutParams.UNSET
    bottomToBottom = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.unsetHorizontally() {
    unsetLeft()
    unsetRight()
}

fun ConstraintLayout.LayoutParams.unsetVertically() {
    unsetTop()
    unsetBottom()
}

fun ConstraintLayout.LayoutParams.leftToLeftOfParent() {
    leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
    leftToRight = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.rightToRightOfParent() {
    rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
    rightToLeft = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.topToTopOfParent() {
    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
    topToBottom = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.bottomToBottomOfParent() {
    bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
    bottomToTop = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.centerOfParent() {
    leftToLeftOfParent()
    rightToRightOfParent()
    topToTopOfParent()
    bottomToBottomOfParent()
}

fun ConstraintLayout.LayoutParams.leftToLeftOf(id: Int) {
    leftToLeft = id
    leftToRight = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.leftToRightOf(id: Int) {
    leftToRight = id
    leftToLeft = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.rightToRightOf(id: Int) {
    rightToRight = id
    rightToLeft = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.rightToLeftOf(id: Int) {
    rightToLeft = id
    rightToRight = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.topToTopOf(id: Int) {
    topToTop = id
    topToBottom = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.topToBottomOf(id: Int) {
    topToBottom = id
    topToTop = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.bottomToBottomOf(id: Int) {
    bottomToBottom = id
    bottomToTop = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.bottomToTopOf(id: Int) {
    bottomToTop = id
    bottomToBottom = ConstraintLayout.LayoutParams.UNSET
}

fun ConstraintLayout.LayoutParams.centerOf(id: Int) {
    leftToLeftOf(id)
    rightToRightOf(id)
    topToTopOf(id)
    bottomToBottomOf(id)
}
