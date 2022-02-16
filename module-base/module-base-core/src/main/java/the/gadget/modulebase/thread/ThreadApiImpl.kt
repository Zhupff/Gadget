package the.gadget.modulebase.thread

import android.os.Handler
import android.os.Looper
import com.google.auto.service.AutoService

@AutoService(ThreadApi::class)
class ThreadApiImpl : ThreadApi {

    private val mainHandler: Handler = Handler(Looper.getMainLooper())

    override fun isOnMainThread(): Boolean = Thread.currentThread() == Looper.getMainLooper().thread

    override fun runOnMainThread(runnable: Runnable) {
        if (isOnMainThread())
            runnable.run()
        else
            mainHandler.post(runnable)
    }

    override fun runOnMainThreadDelay(delay: Long, runnable: Runnable) {
        mainHandler.postDelayed(runnable, delay)
    }

    override fun removeRunnable(runnable: Runnable) {
        mainHandler.removeCallbacks(runnable)
    }
}