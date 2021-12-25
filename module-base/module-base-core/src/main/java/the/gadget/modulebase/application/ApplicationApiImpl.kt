package the.gadget.modulebase.application

import android.app.Application
import com.google.auto.service.AutoService
import java.util.concurrent.atomic.AtomicBoolean

@AutoService(ApplicationApi::class)
class ApplicationApiImpl : ApplicationApi {

    private lateinit var application: Application

    private val hasSetApplication = AtomicBoolean(false)

    override fun setApplication(application: Application) {
        if (hasSetApplication.compareAndSet(false, true)) {
            this.application = application
        } else {
            throw IllegalStateException("Already has set application")
        }
    }

    override fun getApplication(): Application = this.application
}

val APP: ApplicationApiImpl by lazy { ApplicationApi.instance as ApplicationApiImpl }