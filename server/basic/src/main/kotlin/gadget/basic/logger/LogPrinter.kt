package gadget.basic.logger

import com.google.auto.service.AutoService

@AutoService(Logger.Printer::class)
class LogPrinter : Logger.Printer {
    override fun filter(level: Char, label: String): Boolean {
        return true
    }

    override fun print(level: Char, label: String, cause: Throwable?, message: String) {
        when (level) {
            'D', 'd' -> {
                println("[D] %-32s: %s".format(label, message))
            }
            'I', 'i' -> {
                println("[I] %-32s: %s".format(label, message))
            }
            'W', 'w' -> {
                println("[W] %-32s: %s%s".format(label, message, "\n${cause?.stackTraceToString()}"))
            }
        }
    }
}