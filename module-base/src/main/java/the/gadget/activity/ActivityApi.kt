package the.gadget.activity

import android.app.Activity
import android.content.Context
import the.gadget.api.globalApi

interface ActivityApi {
    companion object : ActivityApi by ActivityApi::class.globalApi()

    fun contextToActivity(context: Context?): Activity?

    fun contextToBaseActivity(context: Context?): BaseActivity?
}

fun Context?.toActivity(): Activity? = ActivityApi.contextToActivity(this)

fun Context?.toBaseActivity(): BaseActivity? = ActivityApi.contextToBaseActivity(this)