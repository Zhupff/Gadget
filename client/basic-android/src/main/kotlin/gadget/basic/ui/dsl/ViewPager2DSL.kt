package gadget.basic.ui.dsl

import android.content.Context
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import gadget.basic.annotation.DSLScope

inline fun ViewPager2(
    context: Context,
    params: (@DSLScope ViewPager2).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope ViewPager2).() -> Unit = {},
): ViewPager2 = ViewPager2(context).scope(params, lambda)

inline fun <P : ViewGroup> P.ViewPager2(
    params: (@DSLScope ViewPager2).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope ViewPager2).() -> Unit = {},
): ViewPager2 = scope(ViewPager2(context), params, lambda)
