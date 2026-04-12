package gadget.basic.ui.dsl

import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import gadget.basic.annotation.DslScope

//private typealias TEXT_VIEW = TextView
private typealias TEXT_VIEW = AppCompatTextView

inline fun <V : ViewGroup> ViewScope<V>.TextView(
    params: (@DslScope TEXT_VIEW).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<TEXT_VIEW>).() -> Unit = {},
): TEXT_VIEW = scope(TEXT_VIEW(context), params, lambda)
