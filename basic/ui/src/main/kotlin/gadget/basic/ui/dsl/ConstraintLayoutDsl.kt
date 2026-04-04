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

inline fun <L : ViewGroup> L.ConstraintLayout(
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

inline fun <V : View> V.constraintLayoutParams(
    lambda: (@DslScope ConstraintLayout.LayoutParams).(ConstraintLayout.LayoutParams) -> Unit = {},
): ConstraintLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? ConstraintLayout.LayoutParams ?: ConstraintLayout.LayoutParams(it)
    } ?: ConstraintLayout.LayoutParams(MATCH_CONSTRAINT, MATCH_CONSTRAINT)
    lambda(lp, lp)
    return lp
}

fun <P : ConstraintLayout.LayoutParams> P.unsetLeft() {
    leftToLeft = ConstraintLayout.LayoutParams.UNSET
    leftToRight = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.unsetRight() {
    rightToLeft = ConstraintLayout.LayoutParams.UNSET
    rightToRight = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.unsetTop() {
    topToTop = ConstraintLayout.LayoutParams.UNSET
    topToBottom = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.unsetBottom() {
    bottomToTop = ConstraintLayout.LayoutParams.UNSET
    bottomToBottom = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.unsetHorizontally() {
    unsetLeft()
    unsetRight()
}

fun <P : ConstraintLayout.LayoutParams> P.unsetVertically() {
    unsetTop()
    unsetBottom()
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

fun <P : ConstraintLayout.LayoutParams> P.horizontallyCenterOfParent() {
    leftToLeftOfParent()
    rightToRightOfParent()
}

fun <P : ConstraintLayout.LayoutParams> P.verticallyCenterOfParent() {
    topToTopOfParent()
    bottomToBottomOfParent()
}

fun <P : ConstraintLayout.LayoutParams> P.centerOfParent() {
    horizontallyCenterOfParent()
    verticallyCenterOfParent()
}

fun <P : ConstraintLayout.LayoutParams> P.leftToLeftOf(id: Int) {
    leftToLeft = id
    leftToRight = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.leftToRightOf(id: Int) {
    leftToRight = id
    leftToLeft = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.rightToRightOf(id: Int) {
    rightToRight = id
    rightToLeft = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.rightToLeftOf(id: Int) {
    rightToLeft = id
    rightToRight = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.topToTopOf(id: Int) {
    topToTop = id
    topToBottom = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.topToBottomOf(id: Int) {
    topToBottom = id
    topToTop = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.bottomToBottomOf(id: Int) {
    bottomToBottom = id
    bottomToTop = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.bottomToTopOf(id: Int) {
    bottomToTop = id
    bottomToBottom = ConstraintLayout.LayoutParams.UNSET
}

fun <P : ConstraintLayout.LayoutParams> P.horizontallyCenterOf(id: Int) {
    leftToLeftOf(id)
    rightToRightOf(id)
}

fun <P : ConstraintLayout.LayoutParams> P.verticallyCenterOf(id: Int) {
    topToTopOf(id)
    bottomToBottomOf(id)
}

fun <P : ConstraintLayout.LayoutParams> P.centerOf(id: Int) {
    horizontallyCenterOf(id)
    verticallyCenterOf(id)
}
