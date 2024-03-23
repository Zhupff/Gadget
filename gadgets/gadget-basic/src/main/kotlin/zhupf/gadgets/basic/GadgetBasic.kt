package zhupf.gadgets.basic

import zhupf.gadgets.GadgetDelegate
import zhupf.gadgets.GadgetName

@GadgetName("Basic")
class GadgetBasic : GadgetDelegate() {

    fun jvm(method: String = "implementation") {
        gadgetEx.project.dependencies.add(method, GadgetBasicPublication.dependency("basicJvm"))
    }

    fun android(method: String = "implementation") {
        gadgetEx.project.dependencies.add(method, GadgetBasicPublication.dependency("basicAndroid"))
    }
}