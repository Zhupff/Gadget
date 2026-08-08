package gadget.basic.logger

import android.util.Log
import com.google.auto.service.AutoService

@AutoService(Logger.Printer::class)
class LogPrinter : Logger.Printer {

    override fun filter(level: Char, label: String): Boolean {
        return true;
    }

    override fun print(level: Char, label: String, cause: Throwable?, message: String) {
        when (level) {
            'D', 'd' -> {
                Log.d(label, message)
            }
            'I', 'i' -> {
                Log.i(label, message)
            }
            'W', 'w' -> {
                Log.w(label, message, cause)
            }
        }
    }
}