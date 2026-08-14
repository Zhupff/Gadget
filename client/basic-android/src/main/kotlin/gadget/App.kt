package gadget

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
import gadget.basic.exception.GadgetException
import gadget.basic.logger.Logger
import gadget.basic.tool.mutable
import java.io.File

abstract class App : Application() {

    companion object : IApp, LifecycleOwner by AppLifecycle {
        private val label: String = "App"
        lateinit var instance: App
            private set
        override val processId: Int by lazy {
            android.os.Process.myPid()
        }
        override val processName: String by lazy {
            instance.getSystemService(ActivityManager::class.java)
                .runningAppProcesses.find { it.pid == processId }!!.processName
        }
        override val isMainProcess: Boolean by lazy {
            instance.packageName.equals(processName)
        }
        override val debuggable: Boolean by lazy {
            (instance.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        }
        override val configDir: File by lazy {
            instance.cacheDir.resolve("_CONFIG_").also(File::mkdirs)
        }
        val configuration: LiveData<Configuration> = MutableLiveData()
    }

    init {
        init()
        instance = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Thread.setDefaultUncaughtExceptionHandler(GadgetException)
        Logger.i(label) { "attachBaseContext($base)" }
        registerActivityLifecycleCallbacks(AppLifecycle)
    }

    override fun onCreate() {
        super.onCreate()
        Logger.i(label) { "onCreate" }
        configuration.mutable().value = resources.configuration
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configuration.mutable().value = resources.configuration
    }



    private object AppLifecycle : LifecycleOwner, ActivityLifecycleCallbacks {
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
                Logger.i(label) { "onAppForeground" }
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
                Logger.i(label) { "onAppBackground" }
                (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            }
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (0 == --createdActivities) {
                Logger.i(label) { "onAppDestroyed" }
                (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }
    }
}