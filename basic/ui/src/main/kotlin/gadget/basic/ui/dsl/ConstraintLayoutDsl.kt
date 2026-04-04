package gadget.basic.ui.dsl

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import gadget.basic.annotation.DslScope

inline fun ConstraintLayout(
    context: Context,
    params: LayoutParamsDsl<*, ConstraintLayout>,
    lambda: (@DslScope ConstraintLayout).(ConstraintLayout) -> Unit = {},
): ConstraintLayout = ConstraintLayout(context).apply {
    params.init(this)
    lambda(this, this)
}

inline fun ViewGroup.ConstraintLayout(
    params: LayoutParamsDsl<*, ConstraintLayout>,
    lambda: (@DslScope ConstraintLayout).(ConstraintLayout) -> Unit = {},
): ConstraintLayout = ConstraintLayout(context).also {
    params.init(this, it)
    lambda(it, it)
}

class ConstraintLayoutParams<V : View>(
    size: Pair<Int, Int> = MATCH_CONSTRAINT to MATCH_CONSTRAINT,
    initializer: (@DslScope ConstraintLayout.LayoutParams).(V) -> Unit = {},
) : LayoutParamsDsl<ConstraintLayout.LayoutParams, V>(
    initializer, ConstraintLayout.LayoutParams(size.first, size.second),
)

inline fun View.constraintLayoutParams(
    lambda: (@DslScope ConstraintLayout.LayoutParams).(ConstraintLayout.LayoutParams) -> Unit = {},
): ConstraintLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? ConstraintLayout.LayoutParams ?: ConstraintLayout.LayoutParams(it)
    } ?: ConstraintLayout.LayoutParams(MATCH_CONSTRAINT, MATCH_CONSTRAINT)
    lambda(lp, lp)
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

fun ConstraintLayout.LayoutParams.horizontallyCenterOf(id: Int) {
    leftToLeftOf(id)
    rightToRightOf(id)
}

fun ConstraintLayout.LayoutParams.verticallyCenterOf(id: Int) {
    topToTopOf(id)
    bottomToBottomOf(id)
}

fun ConstraintLayout.LayoutParams.centerOf(id: Int) {
    horizontallyCenterOf(id)
    verticallyCenterOf(id)
}
