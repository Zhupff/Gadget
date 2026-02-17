package gadget.basic.log

import android.util.Log

internal object LogcatPrinter : Logger.Printer {
    override fun print(timestamp: Long, priority: Int, tag: String, cause: Throwable?, content: String) {
        when (priority) {
            Log.VERBOSE -> Log.v(tag, content, cause)
            Log.DEBUG   -> Log.d(tag, content, cause)
            Log.INFO    -> Log.i(tag, content, cause)
            Log.WARN    -> Log.w(tag, content, cause)
            Log.ERROR   -> Log.e(tag, content, cause)
        }
    }
}