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

    override fun toHomeActivity(context: Context) {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }
}

internal val moduleHomeApi: ModuleHomeApiImpl by lazy { ModuleHomeApi.instance as ModuleHomeApiImpl }