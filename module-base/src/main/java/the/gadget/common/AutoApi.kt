@file:JvmName("AutoApi")
package the.gadget.common

import the.gadget.GadgetApplication
import java.util.*

fun <T> autoServiceInstances(cls: Class<T>): ServiceLoader<T> =
    ServiceLoader.load(cls, GadgetApplication.instance.classLoader)