package the.gadget.modulehome

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import com.google.auto.service.AutoService
import the.gadget.modulebase.api.autoServiceInstances
import the.gadget.modulebase.livedata.ArrayListLiveData
import the.gadget.modulebase.livedata.toArrayListLiveData
import the.gadget.modulehome.activity.HomeActivity

@AutoService(ModuleHomeApi::class)
class ModuleHomeApiImpl : ModuleHomeApi {

    private val allHomeApps: ArrayListLiveData<HomeApp> = autoServiceInstances(HomeApp::class.java)
        .toList()
//        .also { it.firstOrNull()?.selected = true }
        .toArrayListLiveData()
        .also { it.commit() }
    fun getAllHomeApps(): LiveData<List<HomeApp>> = allHomeApps

    fun selectHomeApp(app: HomeApp) {
        val selectedApp = allHomeApps.value.find { it.selected }
        if (app == selectedApp) return
        allHomeApps.value.forEach { it.selected = it == app }
        allHomeApps.commit()
    }

    private val allHomeOptions: ArrayListLiveData<HomeOption> = autoServiceInstances(HomeOption::class.java)
        .sortedBy { it.option }
        .toArrayListLiveData()
        .also { it.commit() }
    fun getAllHomeOptions(): LiveData<List<HomeOption>> = allHomeOptions

    override fun toHomeActivity(context: Context) {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }
}

internal val moduleHomeApi: ModuleHomeApiImpl by lazy { ModuleHomeApi.instance as ModuleHomeApiImpl }