package the.gadget.modulebase.application

import android.app.Application
import com.google.auto.service.AutoService
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
}