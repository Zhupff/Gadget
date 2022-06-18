package the.gadget.module.common

import android.app.Application
import com.google.auto.service.AutoService
import the.gadget.api.ApplicationApi
import java.util.concurrent.atomic.AtomicReference

@AutoService(ApplicationApi::class)
class ApplicationApiImpl : ApplicationApi {

    private val applicationRef: AtomicReference<Application> = AtomicReference()

    override fun setApplication(application: Application) {
        if (applicationRef.get() == null) {
            applicationRef.set(application)
        } else {
            throw IllegalStateException("Already has set application!")
        }
    }

    override fun getApplication(): Application = applicationRef.get()

    override fun getPackageName(): String = getApplication().packageName

    override fun getClassLoader(): ClassLoader = getApplication().classLoader

    override fun isDebug(): Boolean = BuildConfig.DEBUG
}

internal val applicationApi: ApplicationApiImpl by lazy { ApplicationApi.instance as ApplicationApiImpl }