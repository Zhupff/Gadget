package the.gadget.modulebase.thread

import java.util.*

interface ThreadApi {
    companion object {
        @JvmStatic
        val instance: ThreadApi by lazy {
            ServiceLoader.load(ThreadApi::class.java).first()
        }
    }

    fun isOnMainThread(): Boolean

    fun runOnMainThread(runnable: Runnable)

    fun runOnMainThreadDelay(delay: Long, runnable: Runnable)

    fun removeRunnable(runnable: Runnable)
}