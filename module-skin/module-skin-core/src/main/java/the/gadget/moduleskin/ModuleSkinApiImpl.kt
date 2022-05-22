package the.gadget.moduleskin

import android.content.Context
import android.content.Intent
import com.google.auto.service.AutoService
import the.gadget.modulebase.common.ToastApi
import the.gadget.moduleskin.activity.SkinActivity

@AutoService(ModuleSkinApi::class)
class ModuleSkinApiImpl : ModuleSkinApi {

    override fun toSkinActivity(context: Context) {
        ToastApi.instance.toast("该功能暂未支持，尽情期待！")
//        context.startActivity(Intent(context, SkinActivity::class.java))
    }
}

internal val moduleSkinApi: ModuleSkinApiImpl by lazy { ModuleSkinApi.instance as ModuleSkinApiImpl }