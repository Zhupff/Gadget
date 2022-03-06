package the.gadget.modulebase.thread

import the.gadget.modulebase.api.apiInstance

interface ThreadApi {
    companion object {
        @JvmStatic
        val instance: ThreadApi by lazy { apiInstance(ThreadApi::class.java) }
    }

    fun isOnMainThread(): Boolean

    fun runOnMainThread(runnable: Runnable)

    fun runOnMainThreadDelay(delay: Long, runnable: Runnable)

    fun removeRunnable(runnable: Runnable)
}