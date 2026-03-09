package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import gadget.basic.annotation.DslScope
import gadget.basic.annotation.ViewDsl

@ViewDsl
private typealias FrameLayout2 = FrameLayout

typealias ViewLayoutParams = ViewGroup.LayoutParams


private val VIEW_ID_CACHE = HashMap<String, Int>()
val String.viewId: Int
    get() = VIEW_ID_CACHE.getOrPut(this) { View.generateViewId() }

inline fun layoutParams(size: Pair<Int, Int>, block: (@DslScope ViewLayoutParams) -> Unit = {}): ViewLayoutParams {
    return ViewLayoutParams(size.first, size.second)
}
