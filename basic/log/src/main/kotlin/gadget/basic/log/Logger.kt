package gadget.basic.log

import android.util.Log
import com.google.auto.service.AutoService
import gadget.basic.Gadget
import java.util.function.Supplier

@AutoService(ILog::class)
class Logger : ILog {

    interface Printer {
        fun print(timestamp: Long, priority: Int, tag: String, cause: Throwable?, content: String)
    }

    private val printers: List<Printer> by lazy {
        if (Gadget.debuggable) {
            listOf(LogcatPrinter)
        } else {
            listOf()
        }
    }

    override fun d(tag: String, content: Supplier<String>) {
        log(Log.DEBUG, tag, null, content)
    }

    override fun i(tag: String, content: Supplier<String>) {
        log(Log.INFO, tag, null, content)
    }

    override fun w(tag: String, cause: Throwable?, content: Supplier<String>) {
        log(Log.WARN, tag, cause, content)
    }

    override fun e(tag: String, cause: Throwable, content: Supplier<String>) {
        log(Log.ERROR, tag, cause, content)
    }

    override fun log(priority: Int, tag: String, cause: Throwable?, content: Supplier<String>) {
        if (!ILog.enable) {
            return
        }
        val timestamp = System.currentTimeMillis()
        val content = content.get()
        printers.forEach { it.print(timestamp, priority, tag, cause, content) }
    }
}