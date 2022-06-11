package the.gadget.api

import android.util.Log

interface LogcatApi {
    companion object {
        @JvmStatic
        val instance: LogcatApi by lazy { apiInstance(LogcatApi::class.java) }
    }

    fun v(tag: String, any: Any?)
    fun d(tag: String, any: Any?)
    fun i(tag: String, any: Any?)
    fun w(tag: String, any: Any?)
    fun e(tag: String, any: Any?)

    fun v(tag: String, msg: String, vararg any: Any?)
    fun d(tag: String, msg: String, vararg any: Any?)
    fun i(tag: String, msg: String, vararg any: Any?)
    fun w(tag: String, msg: String, vararg any: Any?)
    fun e(tag: String, msg: String, vararg any: Any?)
}

val Logcat: LogcatApi by lazy { LogcatApi.instance }

val Any?.hashTag: String; get() = "${this?.javaClass?.simpleName}(${hashCode()})"

fun String.logV(any: Any?) = apply { Logcat.v(this, any) }
fun String.logD(any: Any?) = apply { Logcat.d(this, any) }
fun String.logI(any: Any?) = apply { Logcat.i(this, any) }
fun String.logW(any: Any?) = apply { Logcat.w(this, any) }
fun String.logE(any: Any?) = apply { Logcat.e(this, any) }

fun String.logV(msg: String, vararg any: Any?) = apply { Logcat.v(this, msg, *any) }
fun String.logD(msg: String, vararg any: Any?) = apply { Logcat.d(this, msg, *any) }
fun String.logI(msg: String, vararg any: Any?) = apply { Logcat.i(this, msg, *any) }
fun String.logW(msg: String, vararg any: Any?) = apply { Logcat.w(this, msg, *any) }
fun String.logE(msg: String, vararg any: Any?) = apply { Logcat.e(this, msg, *any) }

fun Any?.logV(any: Any?) = apply { hashTag.logV(any) }
fun Any?.logD(any: Any?) = apply { hashTag.logD(any) }
fun Any?.logI(any: Any?) = apply { hashTag.logI(any) }
fun Any?.logW(any: Any?) = apply { hashTag.logW(any) }
fun Any?.logE(any: Any?) = apply { hashTag.logE(any) }

fun Any?.logV(msg: String, vararg any: Any?) = apply { hashTag.logV(msg, *any) }
fun Any?.logD(msg: String, vararg any: Any?) = apply { hashTag.logD(msg, *any) }
fun Any?.logI(msg: String, vararg any: Any?) = apply { hashTag.logI(msg, *any) }
fun Any?.logW(msg: String, vararg any: Any?) = apply { hashTag.logW(msg, *any) }
fun Any?.logE(msg: String, vararg any: Any?) = apply { hashTag.logE(msg, *any) }


fun String.logW(throwable: Throwable?)   = apply { logW(Log.getStackTraceString(throwable)) }
fun String.logE(throwable: Throwable?)   = apply { logE(Log.getStackTraceString(throwable)) }
fun Any?.logW(throwable: Throwable?)     = apply { logW(Log.getStackTraceString(throwable)) }
fun Any?.logE(throwable: Throwable?)     = apply { logE(Log.getStackTraceString(throwable)) }