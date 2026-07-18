package gadget.basic.logger

import gadget.basic.tool.singleton

interface Logger {

    companion object : Logger by singleton()

    fun d(label: String, message: () -> String): String

    fun i(label: String, message: () -> String): String

    fun w(label: String, cause: Throwable? = null, message: () -> String): String
}
