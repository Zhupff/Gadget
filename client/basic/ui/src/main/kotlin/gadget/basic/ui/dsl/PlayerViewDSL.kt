package gadget.basic.ui.dsl

import android.content.Context
import android.view.ViewGroup
import androidx.media3.ui.PlayerView
import gadget.basic.annotation.DSLScope

inline fun PlayerView(
    context: Context,
    params: (@DSLScope PlayerView).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope PlayerView).() -> Unit = {},
): PlayerView = PlayerView(context).scope(params, lambda)

inline fun <P : ViewGroup> P.PlayerView(
    params: (@DSLScope PlayerView).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope PlayerView).() -> Unit = {},
): PlayerView = scope(PlayerView(context), params, lambda)
