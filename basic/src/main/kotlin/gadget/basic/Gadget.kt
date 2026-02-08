package gadget.basic

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
import gadget.basic.log.Loggable
import gadget.basic.log.logI

open class Gadget : Application(), Loggable by Loggable.Tag(true) {

    companion object {
        lateinit var application: Gadget
            private set

        val debuggable: Boolean by lazy {
            (application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        }
    }

    init {
        Gadget.application = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        logI { "attachBaseContext(${base})" }
    }

    override fun onCreate() {
        super.onCreate()
        logI { "onCreate" }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AppLifecycle.onConfigurationChanged(newConfig)
    }

    object AppLifecycle : LiveData<AppLifecycle.State>(),
        LifecycleOwner, ActivityLifecycleCallbacks,
        Loggable by Loggable.Tag(true) {

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
                value = State.OnAppForeground
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
                value = State.OnAppBackground
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (0 == --createdActivities) {
                logI { "onAppDestroyed" }
                (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                value = State.OnAppDestroyed
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        fun onConfigurationChanged(newConfiguration: Configuration) {
            logI { "onAppConfigurationChanged(${newConfiguration})" }
            value = State.OnAppConfigurationChanged(newConfiguration)
        }

        sealed class State {
            object OnAppForeground : State()
            object OnAppBackground : State()
            object OnAppDestroyed : State()
            class OnAppConfigurationChanged(val newConfiguration: Configuration) : State()
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