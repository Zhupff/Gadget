package the.gadget.module.arch

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

    override fun getVersion(): String = getApplication().packageManager.getPackageInfo(getPackageName(), 0).versionName

    override fun getClassLoader(): ClassLoader = getApplication().classLoader

    override fun isDebug(): Boolean = BuildConfig.DEBUG
}