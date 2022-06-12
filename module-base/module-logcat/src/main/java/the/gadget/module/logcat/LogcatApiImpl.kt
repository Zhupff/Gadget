package the.gadget.module.logcat

import android.util.Log
import com.google.auto.service.AutoService
import the.gadget.api.LogcatApi

@AutoService(LogcatApi::class)
class LogcatApiImpl : LogcatApi {

    override fun v(tag: String, any: Any?) { log(Log.VERBOSE, tag, any.toString()) }
    override fun d(tag: String, any: Any?) { log(Log.DEBUG, tag, any.toString()) }
    override fun i(tag: String, any: Any?) { log(Log.INFO, tag, any.toString()) }
    override fun w(tag: String, any: Any?) { log(Log.WARN, tag, any.toString()) }
    override fun e(tag: String, any: Any?) { log(Log.ERROR, tag, any.toString()) }

    override fun v(tag: String, msg: String, vararg any: Any?) { log(Log.VERBOSE, tag, String.format(msg, *any)) }
    override fun d(tag: String, msg: String, vararg any: Any?) { log(Log.DEBUG, tag, String.format(msg, *any)) }
    override fun i(tag: String, msg: String, vararg any: Any?) { log(Log.INFO, tag, String.format(msg, *any)) }
    override fun w(tag: String, msg: String, vararg any: Any?) { log(Log.WARN, tag, String.format(msg, *any)) }
    override fun e(tag: String, msg: String, vararg any: Any?) { log(Log.ERROR, tag, String.format(msg, *any)) }

    private fun log(level: Int, tag: String, msg: String) {
        Log.println(level, tag, msg)
    }
}