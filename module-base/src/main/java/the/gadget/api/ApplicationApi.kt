package the.gadget.api

import android.app.Application
import java.util.*

interface ApplicationApi {
    companion object {
        val instance: ApplicationApi by lazy {
            ServiceLoader.load(ApplicationApi::class.java).first()
        }
    }

    fun setApplication(application: Application)

    fun getApplication(): Application

    fun getPackageName(): String

    fun getVersion(): String

    fun getClassLoader(): ClassLoader

    fun isDebug(): Boolean
}