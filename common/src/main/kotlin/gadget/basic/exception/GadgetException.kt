package gadget.basic.exception

open class GadgetException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
    val uncaught: ((Thread) -> Boolean)? = null,
) : Throwable(message, cause) {

    companion object : Thread.UncaughtExceptionHandler {
        private val handler = Thread.getDefaultUncaughtExceptionHandler()
        override fun uncaughtException(thread: Thread, throwable: Throwable) {
            if (throwable is GadgetException) {
                if (throwable.uncaught?.invoke(thread) == true) {
                    return
                }
                handler?.uncaughtException(thread, throwable.cause ?: throwable)
            } else {
                handler?.uncaughtException(thread, throwable)
            }
        }
    }
}

fun Throwable.throws(message: String? = null, uncaught: ((Thread) -> Boolean)? = null): Nothing {
    throw if (message.isNullOrBlank()) {
        GadgetException(this.message, this, uncaught)
    } else {
        GadgetException(message, this, uncaught)
    }
}
