package gadget.basic.tool

import android.content.res.Resources
import android.util.TypedValue
import gadget.basic.Gadget

fun Number.dp(resources: Resources = Gadget.application.resources): Float =
    resources.displayMetrics.density * this.toFloat()

fun Number.sp(resources: Resources = Gadget.application.resources): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), resources.displayMetrics)

val Number.dp: Float
    get() = this.dp()

val Number.sp: Float
    get() = this.sp()
