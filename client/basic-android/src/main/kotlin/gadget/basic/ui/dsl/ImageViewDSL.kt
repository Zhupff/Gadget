package gadget.basic.ui.dsl

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import gadget.basic.annotation.DSLScope

inline fun ImageView(
    context: Context,
    params: (@DSLScope ImageView).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope ImageView).() -> Unit = {},
): ImageView = ImageView(context).scope(params, lambda)

inline fun <P : ViewGroup> P.ImageView(
    params: (@DSLScope ImageView).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope ImageView).() -> Unit = {},
): ImageView = scope(ImageView(context), params, lambda)
