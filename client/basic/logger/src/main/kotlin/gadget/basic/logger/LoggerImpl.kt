package gadget.basic.logger

import android.util.Log
import com.google.auto.service.AutoService

@AutoService(Logger::class)
internal class LoggerImpl : Logger {

    override fun d(label: String, message: () -> String): String {
        Log.d(label, message())
        return label
    }

    override fun i(label: String, message: () -> String): String {
        Log.i(label, message())
        return label
    }

    override fun w(label: String, cause: Throwable?, message: () -> String): String {
        Log.w(label, message(), cause)
        return label
    }
}