package gadget.basic.logger

import android.system.Os
import android.util.Log
import androidx.lifecycle.Observer
import gadget.basic.Gadget
import gadget.basic.exception.throws
import gadget.basic.tool.DateTime
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.util.concurrent.atomic.AtomicLong

internal object FilePrinter : Logger.Printer, Observer<Gadget.AppLifecycle.State>, Mutex by Mutex() {

    /** 日志存放目录 */
    private val logDir = Gadget.application.filesDir.resolve("_LOG_").also(File::mkdirs)
    /** 日期工具 */
    private val dateTime = DateTime(DateTime.FORMAT2MS)
    /** 日志条数，阶段统计 */
    private var periodCount: Int = 0
    /** 日志条数，片段统计 */
    private var sessionCount: Int = 0
    /** 日志条数，总数统计 */
    private val totalCount = AtomicLong(0)
    /** 是否需要写入文件 */
    @Volatile private var flushFlag = false
    /** 是否已经标记为释放状态 */
    @Volatile private var releaseFlag = false
    /** 日志文件writer */
    @Volatile private var printWriter: PrintWriter? = null
    /** 日志流 */
    private val loggable = Channel<suspend () -> Unit>(capacity = UNLIMITED)

    init {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.IO) {
            withLock {
                val current = System.currentTimeMillis()
                logDir.listFiles()?.forEach { file ->
                    if (DateTime.getDayDifference(file.lastModified(), current) > 7) {
                        file.delete()
                    }
                }
            }
            withContext(Dispatchers.Main) {
                Gadget.AppLifecycle.observe(Gadget.AppLifecycle, this@FilePrinter)
            }
            while (true) {
                loggable.receive().invoke()
            }
        }
    }

    override fun print(timestamp: Long, priority: Int, tag: String, cause: Throwable?, content: String) {
        val currentCount = totalCount.incrementAndGet()
        val currentTid = Os.gettid()
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.Unconfined) {
            loggable.send {
                withLock {
                    val pw: PrintWriter? = printWriter ?: try {
                        val file = logDir.resolve("${dateTime.format(System.currentTimeMillis())}.txt").also(File::createNewFile)
                        PrintWriter(BufferedWriter(FileWriter(file, true), 1024))
                    } catch (throwable: Throwable) {
                        if (Gadget.debuggable) {
                            throwable.throws("print exception!")
                        } else null
                    }
                    if (pw != null) {
                        printWriter = pw
                        val finalContent = if (cause == null) {
                            content
                        } else {
                            StringBuilder().appendLine(content).appendLine(Log.getStackTraceString(cause)).toString()
                        }
                        pw.println("%s %5s-%-5s %-32s: %s".format(dateTime.format(timestamp), Gadget.AppProcess.id, currentTid, tag, finalContent))
                        if (currentCount >= totalCount.get() || periodCount >= 100 || flushFlag || releaseFlag) {
                            try {
                                pw.checkError()
                                periodCount = 0
                                if (sessionCount >= 100_000) {
                                    sessionCount = 0
                                    printWriter = null
                                    pw.close()
                                }
                            } catch (throwable: Throwable) {
                                if (Gadget.debuggable) {
                                    throwable.throws("flush exception")
                                }
                            } finally {
                                flushFlag = false
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onChanged(value: Gadget.AppLifecycle.State) {
        when (value) {
            is Gadget.AppLifecycle.State.OnAppBackground -> {
                flushFlag = true
            }
            is Gadget.AppLifecycle.State.OnAppDestroyed -> {
                releaseFlag = true
            }
            else -> {
                // ignore
            }
        }
    }
}