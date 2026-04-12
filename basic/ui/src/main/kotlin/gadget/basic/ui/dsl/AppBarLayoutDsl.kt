package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.google.android.material.appbar.AppBarLayout
import gadget.basic.annotation.DslScope

inline fun <V : ViewGroup> ViewScope<V>.AppBarLayout(
    params: (@DslScope AppBarLayout).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<AppBarLayout>).() -> Unit = {},
): AppBarLayout = scope(AppBarLayout(context), params, lambda)

inline fun <V : View> V.appBarLayoutParams(
    width: Int = layoutParams?.width ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    height: Int = layoutParams?.height ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    lambda: (AppBarLayout.LayoutParams).() -> Unit = {},
): AppBarLayout.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? AppBarLayout.LayoutParams ?: AppBarLayout.LayoutParams(it)
    } ?: AppBarLayout.LayoutParams(width, height)
    lambda(lp)
    if (this.layoutParams != null) {
        this.layoutParams = lp
    }
    return lp
}
