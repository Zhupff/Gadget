package gadget.basic.tool

import android.content.res.Resources
import android.util.TypedValue
import gadget.basic.Gadget

fun Number.dp(resources: Resources = Gadget.application.resources): Float =
    resources.displayMetrics.density * this.toFloat()

fun Number.sp(resources: Resources = Gadget.application.resources): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), resources.displayMetrics)

val Float.dp: Float
    get() = this.dp()

val Float.sp: Float
    get() = this.sp()

val Int.dp: Int
    get() = this.dp().toInt()

val Int.sp: Int
    get() = this.sp().toInt()
