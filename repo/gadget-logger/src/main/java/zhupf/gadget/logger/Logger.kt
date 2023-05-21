package zhupf.gadget.logger

import android.util.Log
import android.widget.Toast
import androidx.annotation.IntDef
import kotlin.reflect.KClass

object GLog {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, Log.ERROR, Log.ASSERT)
    annotation class LevelRange

    @LevelRange var minLevel: Int = Log.VERBOSE

    @LevelRange var maxLevel: Int = Log.ERROR

    @JvmStatic
    fun v(tag: Any, info: Any?): String = log(Log.VERBOSE, tag, info)

    @JvmStatic
    fun d(tag: Any, info: Any?): String = log(Log.DEBUG, tag, info)

    @JvmStatic
    fun i(tag: Any, info: Any?): String = log(Log.INFO, tag, info)

    @JvmStatic
    fun w(tag: Any, info: Any?): String = log(Log.WARN, tag, info)

    @JvmStatic
    fun e(tag: Any, info: Any?): String = log(Log.ERROR, tag, info)

    @JvmStatic
    fun s(tag: Any, deep: Int = 4, @LevelRange level: Int = Log.DEBUG): String {
        val tagStr = tag.asTag
        val stack = Thread.currentThread().stackTrace
        for (i in deep until stack.size) {
            log(level, tagStr, stack[i])
        }
        Toast.LENGTH_LONG
        return tagStr
    }

    private fun log(level: Int, tag: Any, info: Any?): String = log(level, tag.asTag, info.asInfo)

    private fun log(level: Int, tag: String, info: String): String {
        if (level in minLevel..maxLevel) {
            Log.println(level, tag, info)
        }
        return tag
    }

    private val Any.asTag: String; get() = when (this) {
        is String -> this
        is KClass<*> -> java.simpleName
        is Class<*> -> simpleName
        else -> "${javaClass.simpleName}(${hashCode()})"
    }

    private val Any?.asInfo: String; get() = when (this) {
        is String -> this
        is Throwable -> Log.getStackTraceString(this)
        is StackTraceElement -> "${this.className}-${this.methodName} (${this.fileName}:${this.lineNumber})"
        else -> this.toString()
    }
}

fun Any.logV(any: Any?): String = GLog.v(this, any)
fun Any.logD(any: Any?): String = GLog.d(this, any)
fun Any.logI(any: Any?): String = GLog.i(this, any)
fun Any.logW(any: Any?): String = GLog.w(this, any)
fun Any.logE(any: Any?): String = GLog.e(this, any)
fun Any.logS(deep: Int = 5, @GLog.LevelRange level: Int = Log.DEBUG): String = GLog.s(this, deep, level)