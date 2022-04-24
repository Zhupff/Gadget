package the.gadget.modulehome

import android.content.Context
import android.content.Intent
import com.google.auto.service.AutoService

@AutoService(ModuleHomeApi::class)
class ModuleHomeApiImpl : ModuleHomeApi {

    override fun toHomeActivity(context: Context) {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }
}