package gadget.basic

import android.R.attr.value
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gadget.basic.logger.Loggable
import gadget.basic.logger.logI
import gadget.basic.logger.loggable
import gadget.basic.tool.mutable
import gadget.basic.window.WindowState

open class Gadget : Application(), Loggable {

    companion object {
        lateinit var application: Gadget
            private set

        val debuggable: Boolean by lazy {
            (application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        }

        val configuration: LiveData<Configuration> = MutableLiveData()
    }

    override val loggable: String = loggable(true)

    init {
        Gadget.application = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        logI { "attachBaseContext(${base})" }
        registerActivityLifecycleCallbacks(AppLifecycle)
    }

    override fun onCreate() {
        super.onCreate()
        logI { "onCreate" }
        configuration.mutable().value = resources.configuration
        WindowState.init()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configuration.mutable().value = newConfig
    }

    object AppLifecycle : LifecycleOwner, ActivityLifecycleCallbacks, Loggable {

        override val loggable: String = loggable(true)

        override val lifecycle: Lifecycle = LifecycleRegistry(this)

        private var createdActivities = 0
        private var startedActivities = 0
        private var resumedActivities = 0

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (0 == createdActivities++) {
                (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            }
        }

        override fun onActivityStarted(activity: Activity) {
            if (0 == startedActivities++) {
                logI { "onAppForeground" }
                (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_START)
            }
        }

        override fun onActivityResumed(activity: Activity) {
            if (0 == resumedActivities++) {
                (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            }
        }

        override fun onActivityPaused(activity: Activity) {
            if (0 == --resumedActivities) {
                (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            }
        }

        override fun onActivityStopped(activity: Activity) {
            if (0 == --startedActivities) {
                logI { "onAppBackground" }
                (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (0 == --createdActivities) {
                logI { "onAppDestroyed" }
                (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }
    }

    object AppProcess {
        val id: Int by lazy { android.os.Process.myPid() }
        val name: String by lazy {
            Gadget.application.getSystemService(ActivityManager::class.java).runningAppProcesses.find {
                it.pid == id
            }!!.processName
        }
        val isMain: Boolean by lazy {
            Gadget.application.packageName.equals(name)
        }
    }
}