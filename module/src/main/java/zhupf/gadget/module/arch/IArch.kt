package zhupf.gadget.module.arch

import zhupf.gadget.Gadget
import zhupf.gadget.of.OfLoader

interface IArch {
    companion object : IArch by implement()
}

inline fun <reified T> implement(vararg args: Any?): T = OfLoader(Gadget.application.classLoader, T::class.java).load().create(*args).first()