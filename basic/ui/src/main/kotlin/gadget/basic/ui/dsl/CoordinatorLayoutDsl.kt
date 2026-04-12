package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.coordinatorlayout.widget.CoordinatorLayout
import gadget.basic.annotation.DslScope

inline fun <V : ViewGroup> ViewScope<V>.CoordinatorLayout(
    params: (@DslScope CoordinatorLayout).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<CoordinatorLayout>).() -> Unit = {},
): CoordinatorLayout = scope(CoordinatorLayout(context), params, lambda)

inline fun <V : View> V.coordinatorLayoutParams(
    width: Int = layoutParams?.width ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    height: Int = layoutParams?.height ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    lambda: (CoordinatorLayout.LayoutParams).() -> Unit = {},
): CoordinatorLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? CoordinatorLayout.LayoutParams ?: CoordinatorLayout.LayoutParams(it)
    } ?: CoordinatorLayout.LayoutParams(width, height)
    lambda(lp)
    if (this.layoutParams != null) {
        this.layoutParams = lp
    }
    return lp
}
