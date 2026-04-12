package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import gadget.basic.annotation.DslScope

inline fun <V : ViewGroup> ViewScope<V>.RecyclerView(
    params: (@DslScope RecyclerView).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<RecyclerView>).() -> Unit = {},
): RecyclerView = scope(RecyclerView(context), params, lambda)

inline fun <V : View> V.recyclerViewLayoutParams(
    width: Int = layoutParams?.width ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    height: Int = layoutParams?.height ?: if (this is ViewGroup) MATCH_PARENT else WRAP_CONTENT,
    lambda: (RecyclerView.LayoutParams).() -> Unit = {},
): RecyclerView.LayoutParams {
    val lp = this.layoutParams?.let {
        it as? RecyclerView.LayoutParams ?: RecyclerView.LayoutParams(it)
    } ?: RecyclerView.LayoutParams(width, height)
    lambda(lp)
    if (this.layoutParams != null) {
        this.layoutParams = lp
    }
    return lp
}
