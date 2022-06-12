package the.gadget.component.theme

import android.content.Context
import android.content.Intent
import com.google.auto.service.AutoService
import the.gadget.api.ToastApi

@AutoService(ComponentThemeApi::class)
class ComponentThemeApiImpl : ComponentThemeApi {

    override fun toThemeActivity(context: Context) {
        ToastApi.instance.toast("该功能暂未支持，尽情期待！")
//        context.startActivity(Intent(context, ThemeActivity::class.java))
    }
}

internal val componentThemeApi: ComponentThemeApiImpl by lazy { ComponentThemeApi.instance as ComponentThemeApiImpl }