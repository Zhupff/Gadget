package gadget.basic.ui.dsl

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import gadget.basic.annotation.DSLScope

inline fun TextView(
    context: Context,
    params: (@DSLScope TextView).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope TextView).() -> Unit = {},
): TextView = TextView(context).scope(params, lambda)

inline fun <P : ViewGroup> P.TextView(
    params: (@DSLScope TextView).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope TextView).() -> Unit = {},
): TextView = scope(TextView(context), params, lambda)
