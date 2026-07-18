package gadget.basic.tool

import java.util.ServiceLoader

inline fun <reified T> singleton(): T = ServiceLoader.load<T>(T::class.java, T::class.java.classLoader).first()

inline fun <reified T> iteration(): List<T> = ServiceLoader.load<T>(T::class.java, T::class.java.classLoader).toList()
