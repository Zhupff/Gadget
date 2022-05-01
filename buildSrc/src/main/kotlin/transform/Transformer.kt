package transform

import logI

abstract class Transformer {

    open fun beforeTransform(context: TransformContext) {
        logI("beforeTransform.")
    }

    open fun afterTransform(context: TransformContext) {
        logI("afterTransform.")
    }

    fun handleDirClass(className: String, classBytes: ByteArray): ByteArray =
        if (filterDirClass(className)) transformDirClass(className, classBytes) else classBytes

    fun handleJarClass(className: String, classBytes: ByteArray): ByteArray =
        if (filterJarClass(className)) transformJarClass(className, classBytes) else classBytes

    open fun filterClass(className: String): Boolean = false

    open fun filterDirClass(className: String): Boolean = filterClass(className)

    open fun filterJarClass(className: String): Boolean = filterClass(className)

    open fun transformClass(className: String, classBytes: ByteArray): ByteArray = classBytes

    open fun transformDirClass(className: String, classBytes: ByteArray): ByteArray = transformClass(className, classBytes)

    open fun transformJarClass(className: String, classBytes: ByteArray): ByteArray = transformClass(className, classBytes)
}