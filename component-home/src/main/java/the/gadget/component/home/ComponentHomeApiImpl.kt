package the.gadget.component.home

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import the.gadget.api.GlobalApi
import the.gadget.common.autoServiceInstances
import the.gadget.component.home.activity.HomeActivity
import the.gadget.livedata.ArrayListLiveData
import the.gadget.livedata.toArrayListLiveData

@GlobalApi(ComponentHomeApi::class)
class ComponentHomeApiImpl : ComponentHomeApi {

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

internal val componentHomeApi: ComponentHomeApiImpl by lazy { ComponentHomeApi.instance as ComponentHomeApiImpl }