package the.gadget.component.skin

import android.content.Context
import com.google.auto.service.AutoService
import the.gadget.api.ToastApi

@AutoService(ComponentSkinApi::class)
class ComponentSkinApiImpl : ComponentSkinApi {

    override fun toSkinActivity(context: Context) {
        ToastApi.instance.toast("该功能暂未支持，尽情期待！")
//        context.startActivity(Intent(context, SkinActivity::class.java))
    }
}

internal val componentSkinApi: ComponentSkinApiImpl by lazy { ComponentSkinApi.instance as ComponentSkinApiImpl }