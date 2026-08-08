package gadget

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File

abstract class App : Application(), IApp {

    companion object {
        lateinit var instance: App
            private set
    }

    init {
        init()
        instance = this
    }

    override val debuggable: Boolean by lazy {
        (this.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    override val configDir: File by lazy {
        this.cacheDir.resolve("_config_").also(File::mkdirs)
    }

    val configuration: LiveData<Configuration> = MutableLiveData()
}