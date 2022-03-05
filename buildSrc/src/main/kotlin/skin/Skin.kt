package skin

import groovy.lang.Closure

object Skin {

    private val composeDelegate = Compose()

    internal val allSkinInfo: List<SkinInfo>; get() = composeDelegate.allSkinInfo

    @JvmStatic
    fun compose(closure: Closure<*>) {
        composeDelegate.delegate(closure)
    }

    data class SkinInfo(val id: Int, val name: String, val prefix: String)

    private class Compose {
        val allSkinInfo = mutableListOf<SkinInfo>()
        fun register(id: Int, name: String, prefix: String) {
            allSkinInfo.add(SkinInfo(id, name, prefix))
        }
        fun delegate(closure: Closure<*>) {
            allSkinInfo.clear()
            closure.delegate = this
            closure.call()
        }
    }
}