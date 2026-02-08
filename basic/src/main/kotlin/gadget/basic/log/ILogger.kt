package gadget.basic.log

import gadget.basic.Gadget
import gadget.basic.tool.singleton
import java.util.function.Supplier

interface ILogger {

    companion object : ILogger by singleton() {
        @Volatile var enable: Boolean = Gadget.debuggable
            set(value) {
                if (!Gadget.debuggable) {
                    field = value
                }
            }
    }

    fun d(tag: String, content: Supplier<String>)

    fun i(tag: String, content: Supplier<String>)

    fun w(tag: String, cause: Throwable?, content: Supplier<String>)

    fun e(tag: String, cause: Throwable, content: Supplier<String>)

    fun log(priority: Int, tag: String, cause: Throwable?, content: Supplier<String>)
}

fun String.logD(content: Supplier<String>): String = apply {
    ILogger.d(this, content)
}

fun String.logI(content: Supplier<String>): String = apply {
    ILogger.i(this, content)
}

fun String.logW(cause: Throwable?, content: Supplier<String>): String = apply {
    ILogger.w(this, cause, content)
}

fun String.logE(cause: Throwable, content: Supplier<String>): String = apply {
    ILogger.e(this, cause, content)
}

fun Loggable.logD(content: Supplier<String>): String {
    ILogger.d(loggableTag, content)
    return loggableTag
}

fun Loggable.logI(content: Supplier<String>): String {
    ILogger.i(loggableTag, content)
    return loggableTag
}

fun Loggable.logW(cause: Throwable?, content: Supplier<String>): String {
    ILogger.w(loggableTag, cause, content)
    return loggableTag
}

fun Loggable.logE(cause: Throwable, content: Supplier<String>): String {
    ILogger.e(loggableTag, cause, content)
    return loggableTag
}
