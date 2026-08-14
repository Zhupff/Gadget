package gadget

import android.content.Context
import gadget.basic.exception.throws
import gadget.basic.tool.iteration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.TimeoutException

class RealApp : App() {

    private val startups = mutableListOf<IApp.Startup>()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (isMainProcess) {
            startups.addAll(iteration<IApp.Startup.MainStartup>().sortedBy { it.priority })
        }
    }

    override fun onCreate() {
        super.onCreate()
        runBlocking {
            withTimeoutOrNull(if (debuggable) 300L else 3_000L) {
                startups.groupBy { it.priority }.forEach { (_, group) ->
                    channelFlow {
                        group.forEach { startup ->
                            launch(Dispatchers.Default) {
                                startup.post()
                                send(startup)
                            }
                        }
                    }.collect { startup ->
                        startup.run()
                    }
                }
            } ?: TimeoutException("Startup timeout").throws()
        }
    }
}