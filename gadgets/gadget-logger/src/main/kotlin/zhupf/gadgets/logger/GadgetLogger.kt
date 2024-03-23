package zhupf.gadgets.logger

import zhupf.gadgets.GadgetDelegate
import zhupf.gadgets.GadgetName

@GadgetName("Logger")
class GadgetLogger : GadgetDelegate() {

    fun logger(method: String = "implementation") {
        gadgetEx.project.dependencies.add(method, GadgetLoggerPublication.dependency("logger"))
    }
}