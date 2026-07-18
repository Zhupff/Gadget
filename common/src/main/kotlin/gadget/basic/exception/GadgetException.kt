package gadget.basic.exception

open class GadgetException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
    val uncaught: ((Thread) -> Unit)? = null,
) : Throwable(message, cause) {

    companion object : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(thread: Thread, throwable: Throwable) {
            if (throwable is GadgetException) {
                throwable.uncaught?.invoke(thread)
            }
        }
    }
}

fun Throwable.throws(message: String? = null, uncaught: ((Thread) -> Unit)? = null): Nothing {
    throw if (message.isNullOrBlank()) {
        GadgetException(this.message, this, uncaught)
    } else {
        GadgetException(message, this, uncaught)
    }
}
