package the.gadget.modulebase.skinresource

import java.util.*

interface SRApi {
    companion object {
        @JvmStatic
        val instance: SRApi by lazy {
            ServiceLoader.load(SRApi::class.java).first()
        }
    }
}