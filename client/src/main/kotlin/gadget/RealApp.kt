package gadget

import android.content.Context
import gadget.basic.exception.GadgetException
import gadget.basic.exception.throws
import gadget.basic.tool.iteration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

class RealApp : App() {

    private val tasks: MutableList<Deferred<Unit>> = mutableListOf()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Thread.setDefaultUncaughtExceptionHandler(GadgetException)
        CoroutineScope(Dispatchers.Main).launch {
            iteration<IApp.Task>()
                .sortedBy { it.priority }
                .map { async { it.execute() } }
                .let(tasks::addAll)
        }
    }

    override fun onCreate() {
        super.onCreate()
        try {
            runBlocking {
                withTimeout(500L) {
                    tasks.awaitAll()
                }
            }
        } catch (exception: TimeoutCancellationException) {
            exception.throws("App tasks execute timeout!")
        }
    }
}