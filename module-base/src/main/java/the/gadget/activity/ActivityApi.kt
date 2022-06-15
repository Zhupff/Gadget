package the.gadget.activity

import android.app.Activity
import android.content.Context
import the.gadget.api.apiInstance

interface ActivityApi {
    companion object {
        val instance: ActivityApi by lazy { apiInstance(ActivityApi::class.java) }
    }

    fun contextToActivity(context: Context?): Activity?

    fun contextToBaseActivity(context: Context?): BaseActivity?
}

fun Context?.toActivity(): Activity? = ActivityApi.instance.contextToActivity(this)

fun Context?.toBaseActivity(): BaseActivity? = ActivityApi.instance.contextToBaseActivity(this)