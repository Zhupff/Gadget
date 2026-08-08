package gadget.basic.tool

import android.content.res.Resources
import android.util.TypedValue
import gadget.App

fun Number.dp(resources: Resources = App.instance.resources): Float =
    resources.displayMetrics.density * this.toFloat()

fun Number.sp(resources: Resources = App.instance.resources): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), resources.displayMetrics)

val Float.dp: Float
    get() = this.dp()

val Float.sp: Float
    get() = this.sp()

val Int.dp: Int
    get() = this.dp().toInt()

val Int.sp: Int
    get() = this.sp().toInt()
