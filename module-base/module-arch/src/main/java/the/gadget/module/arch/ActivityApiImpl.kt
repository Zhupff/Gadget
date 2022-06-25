package the.gadget.module.arch

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.google.auto.service.AutoService
import the.gadget.activity.ActivityApi
import the.gadget.activity.BaseActivity

@AutoService(ActivityApi::class)
class ActivityApiImpl : ActivityApi {

    override fun contextToActivity(context: Context?): Activity? {
        var ctx = context
        while (ctx != null) {
            if (ctx is Activity)
                return ctx
            else if (ctx is ContextWrapper)
                ctx = ctx.baseContext
            else
                break
        }
        return null
    }

    override fun contextToBaseActivity(context: Context?): BaseActivity? = contextToActivity(context) as? BaseActivity
}