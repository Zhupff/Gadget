package zhupf.gadgets.blur

import zhupf.gadgets.GadgetDelegate
import zhupf.gadgets.GadgetName

@GadgetName("Blur")
class GadgetBlur : GadgetDelegate() {

    fun blur(method: String = "implementation") {
        gadgetEx.project.dependencies.add(method, GadgetBlurPublication.dependency("blur"))
    }
}