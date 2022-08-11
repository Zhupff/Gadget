package the.gadget.component.home

import androidx.lifecycle.LiveData
import the.gadget.common.autoServiceInstances
import the.gadget.livedata.ArrayListLiveData
import the.gadget.livedata.toArrayListLiveData

class ComponentHomeApiImpl {

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
}

internal val componentHomeApi: ComponentHomeApiImpl by lazy { ComponentHomeApiImpl() }