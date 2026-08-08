package gadget.basic.logger

import gadget.basic.tool.iteration

object Logger {

    interface Printer {
        fun filter(level: Char, label: String): Boolean
        fun print(level: Char, label: String, cause: Throwable?, message: String)
    }

    private val printers = iteration<Printer>()

    fun d(label: String, message: () -> String) {
        var content: String? = null
        printers.forEach { printer ->
            if (printer.filter('D', label)) {
                if (content == null) {
                    content = message()
                }
                printer.print('D', label, null, content)
            }
        }
    }

    fun i(label: String, message: () -> String) {
        var content: String? = null
        printers.forEach { printer ->
            if (printer.filter('I', label)) {
                if (content == null) {
                    content = message()
                }
                printer.print('I', label, null, content)
            }
        }
    }

    fun w(label: String, cause: Throwable? = null, message: () -> String) {
        var content: String? = null
        printers.forEach { printer ->
            if (printer.filter('W', label)) {
                if (content == null) {
                    content = message()
                }
                printer.print('W', label, cause, content)
            }
        }
    }
}
