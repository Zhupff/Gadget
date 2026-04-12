package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.google.android.material.appbar.CollapsingToolbarLayout
import gadget.basic.annotation.DslScope

inline fun <V : ViewGroup> ViewScope<V>.CollapsingToolbarLayout(
    params: (@DslScope CollapsingToolbarLayout).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<CollapsingToolbarLayout>).() -> Unit = {},
): CollapsingToolbarLayout = scope(CollapsingToolbarLayout(context), params, lambda)

inline fun <V : View> V.collapsingToolbarLayoutParams(
    width: Int = layoutParams?.width ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    height: Int = layoutParams?.height ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    lambda: (CollapsingToolbarLayout.LayoutParams).() -> Unit = {},
): CollapsingToolbarLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? CollapsingToolbarLayout.LayoutParams ?: CollapsingToolbarLayout.LayoutParams(it)
    } ?: CollapsingToolbarLayout.LayoutParams(width, height)
    lambda(lp)
    if (this.layoutParams != null) {
        this.layoutParams = lp
    }
    return lp
}
