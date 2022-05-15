package the.gadget.modulehome

import android.content.Context
import androidx.annotation.DrawableRes
import the.gadget.modulebase.api.apiInstanceOrNull
import the.gadget.modulebase.fragment.BaseFragment

interface ModuleHomeApi {
    companion object {
        val instance: ModuleHomeApi? by lazy { apiInstanceOrNull(ModuleHomeApi::class.java) }
    }

    abstract class HomeApp(val id: String, val name: String, @DrawableRes val icon: Int) {
        abstract fun newFragment(): BaseFragment
    }

    fun toHomeActivity(context: Context)
}