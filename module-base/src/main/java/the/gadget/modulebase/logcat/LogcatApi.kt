package the.gadget.modulebase.logcat

import android.util.Log
import the.gadget.modulebase.api.apiInstance

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

fun String.logV(any: Any?) { Logcat.v(this, any) }
fun String.logD(any: Any?) { Logcat.d(this, any) }
fun String.logI(any: Any?) { Logcat.i(this, any) }
fun String.logW(any: Any?) { Logcat.w(this, any) }
fun String.logE(any: Any?) { Logcat.e(this, any) }

fun String.logV(msg: String, vararg any: Any?) { Logcat.v(this, msg, *any) }
fun String.logD(msg: String, vararg any: Any?) { Logcat.d(this, msg, *any) }
fun String.logI(msg: String, vararg any: Any?) { Logcat.i(this, msg, *any) }
fun String.logW(msg: String, vararg any: Any?) { Logcat.w(this, msg, *any) }
fun String.logE(msg: String, vararg any: Any?) { Logcat.e(this, msg, *any) }

fun Class<*>.logV(any: Any?) { this.simpleName.logV(any) }
fun Class<*>.logD(any: Any?) { this.simpleName.logD(any) }
fun Class<*>.logI(any: Any?) { this.simpleName.logI(any) }
fun Class<*>.logW(any: Any?) { this.simpleName.logW(any) }
fun Class<*>.logE(any: Any?) { this.simpleName.logE(any) }

fun Class<*>.logV(msg: String, vararg any: Any?) { this.simpleName.logV(msg, *any) }
fun Class<*>.logD(msg: String, vararg any: Any?) { this.simpleName.logD(msg, *any) }
fun Class<*>.logI(msg: String, vararg any: Any?) { this.simpleName.logI(msg, *any) }
fun Class<*>.logW(msg: String, vararg any: Any?) { this.simpleName.logW(msg, *any) }
fun Class<*>.logE(msg: String, vararg any: Any?) { this.simpleName.logE(msg, *any) }

fun Any?.logV(any: Any?) { "${this?.javaClass?.simpleName}(${hashCode()})".logV(any) }
fun Any?.logD(any: Any?) { "${this?.javaClass?.simpleName}(${hashCode()})".logD(any) }
fun Any?.logI(any: Any?) { "${this?.javaClass?.simpleName}(${hashCode()})".logI(any) }
fun Any?.logW(any: Any?) { "${this?.javaClass?.simpleName}(${hashCode()})".logW(any) }
fun Any?.logE(any: Any?) { "${this?.javaClass?.simpleName}(${hashCode()})".logE(any) }

fun Any?.logV(msg: String, vararg any: Any?) { "${this?.javaClass?.simpleName}(${hashCode()})".logV(msg, *any) }
fun Any?.logD(msg: String, vararg any: Any?) { "${this?.javaClass?.simpleName}(${hashCode()})".logD(msg, *any) }
fun Any?.logI(msg: String, vararg any: Any?) { "${this?.javaClass?.simpleName}(${hashCode()})".logI(msg, *any) }
fun Any?.logW(msg: String, vararg any: Any?) { "${this?.javaClass?.simpleName}(${hashCode()})".logW(msg, *any) }
fun Any?.logE(msg: String, vararg any: Any?) { "${this?.javaClass?.simpleName}(${hashCode()})".logE(msg, *any) }


fun String.logW(throwable: Throwable?) { logW(Log.getStackTraceString(throwable)) }
fun String.logE(throwable: Throwable?) { logE(Log.getStackTraceString(throwable)) }
fun Class<*>.logW(throwable: Throwable?) { logW(Log.getStackTraceString(throwable)) }
fun Class<*>.logE(throwable: Throwable?) { logE(Log.getStackTraceString(throwable)) }
fun Any?.logW(throwable: Throwable?) { logW(Log.getStackTraceString(throwable)) }
fun Any?.logE(throwable: Throwable?) { logE(Log.getStackTraceString(throwable)) }