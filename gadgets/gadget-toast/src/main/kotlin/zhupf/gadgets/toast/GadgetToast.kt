package zhupf.gadgets.toast

import zhupf.gadgets.GadgetDelegate
import zhupf.gadgets.GadgetName

@GadgetName("Toast")
class GadgetToast : GadgetDelegate() {

    fun toast(method: String = "implementation") {
        gadgetEx.project.dependencies.add(method, GadgetToastPublication.dependency("toast"))
    }
}