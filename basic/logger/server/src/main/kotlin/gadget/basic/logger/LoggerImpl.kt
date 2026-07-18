package gadget.basic.logger

import com.google.auto.service.AutoService

@AutoService(Logger::class)
class LoggerImpl : Logger {

    override fun d(label: String, message: () -> String): String {
        println("[D] %-32s: %s".format(label, message()))
        return label
    }

    override fun i(label: String, message: () -> String): String {
        println("[I] %-32s: %s".format(label, message()))
        return label
    }

    override fun w(label: String, cause: Throwable?, message: () -> String): String {
        println("[W] %-32s: %s%s".format(label, message(), "\n${cause?.stackTraceToString()}"))
        return label
    }
}