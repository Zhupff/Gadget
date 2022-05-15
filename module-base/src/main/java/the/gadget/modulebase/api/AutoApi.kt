@file:JvmName("AutoApi")
package the.gadget.modulebase.api

import the.gadget.modulebase.application.ApplicationApi
import java.util.*

fun <T> autoServiceInstances(cls: Class<T>): ServiceLoader<T> =
    ServiceLoader.load(cls, ApplicationApi.instance.getClassLoader())

fun <T> apiInstance(cls: Class<T>): T = autoServiceInstances(cls).first()

fun <T> apiInstanceOrNull(cls: Class<T>): T? = autoServiceInstances(cls).firstOrNull()