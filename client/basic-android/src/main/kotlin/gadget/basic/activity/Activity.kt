package gadget.basic.activity

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import gadget.basic.exception.throws

fun Context.activityOrNull(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) {
            return ctx
        }
        ctx = ctx.baseContext
    }
    return null
}

fun Context.requiredActivity(): Activity {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) {
            return ctx
        }
        ctx = ctx.baseContext
    }
    IllegalStateException("Activity Not Found!").throws()
}