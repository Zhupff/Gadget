package the.gadget.common

import android.util.Log
import the.gadget.api.globalApi

interface LogcatApi {
    companion object : LogcatApi by LogcatApi::class.globalApi()

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

val Any?.hashTag: String; get() = "${this?.javaClass?.simpleName}(${hashCode()})"

fun String.logV(any: Any?) = apply { LogcatApi.v(this, any) }
fun String.logD(any: Any?) = apply { LogcatApi.d(this, any) }
fun String.logI(any: Any?) = apply { LogcatApi.i(this, any) }
fun String.logW(any: Any?) = apply { LogcatApi.w(this, any) }
fun String.logE(any: Any?) = apply { LogcatApi.e(this, any) }

fun String.logV(msg: String, vararg any: Any?) = apply { LogcatApi.v(this, msg, *any) }
fun String.logD(msg: String, vararg any: Any?) = apply { LogcatApi.d(this, msg, *any) }
fun String.logI(msg: String, vararg any: Any?) = apply { LogcatApi.i(this, msg, *any) }
fun String.logW(msg: String, vararg any: Any?) = apply { LogcatApi.w(this, msg, *any) }
fun String.logE(msg: String, vararg any: Any?) = apply { LogcatApi.e(this, msg, *any) }

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