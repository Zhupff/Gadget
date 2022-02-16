package skinresource

import groovy.lang.Closure

object SR {

    private val composeDelegate = Compose()

    internal val allSkinInfo: List<SkinInfo>; get() = composeDelegate.allSResInfo

    @JvmStatic
    fun compose(closure: Closure<*>) {
        composeDelegate.delegate(closure)
    }

    data class SkinInfo(val id: Int, val name: String, val prefix: String)

    private class Compose {
        val allSResInfo = mutableListOf<SkinInfo>()
        fun register(id: Int, name: String, prefix: String) {
            allSResInfo.add(SkinInfo(id, name, prefix))
        }
        fun delegate(closure: Closure<*>) {
            allSResInfo.clear()
            closure.delegate = this
            closure.call()
        }
    }
}