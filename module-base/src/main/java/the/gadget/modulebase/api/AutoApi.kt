@file:JvmName("AutoApi")
package the.gadget.modulebase.api

import the.gadget.modulebase.application.ApplicationApi
import java.util.*

fun <T> apiInstance(cls: Class<T>): T =
    ServiceLoader.load(cls, ApplicationApi.instance.getClassLoader()).first()

fun <T> apiInstanceOrNull(cls: Class<T>): T? =
    ServiceLoader.load(cls, ApplicationApi.instance.getClassLoader()).firstOrNull()