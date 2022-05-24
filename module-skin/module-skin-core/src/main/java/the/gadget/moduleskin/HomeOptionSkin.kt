package the.gadget.moduleskin

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.google.auto.service.AutoService
import the.gadget.modulebase.livedata.observeLifecycleOrForever
import the.gadget.modulebase.skin.SkinApi
import the.gadget.modulebase.skin.SkinPackage
import the.gadget.modulebase.weight.listener.ViewOnAttachStateChangeListener
import the.gadget.modulebase.weight.recyclerview.BindingRecyclerViewHolder
import the.gadget.modulebase.weight.recyclerview.RecyclerViewHolder
import the.gadget.modulehome.HomeOption
import the.gadget.moduleskincore.R
import the.gadget.moduleskincore.databinding.HomeOptionSkinViewHolderBinding

@AutoService(HomeOption::class)
class HomeOptionSkin : HomeOption(Option.Skin) {

    override fun createViewHolder(container: ViewGroup): RecyclerViewHolder =
        ViewHolder(container, R.layout.home_option_skin_view_holder).also { it.onCreate() }

    override fun bindViewHolder(viewHolder: RecyclerViewHolder) { (viewHolder as ViewHolder).onBind()}

    private class ViewHolder(container: ViewGroup, id: Int) : BindingRecyclerViewHolder<HomeOptionSkinViewHolderBinding>(container, id) {

        fun onCreate() {
            itemView.addOnAttachStateChangeListener(object : ViewOnAttachStateChangeListener() {
                private val observer = Observer<SkinPackage> { onBind() }
                override fun onViewAttachedToWindow(v: View?) {
                    SkinApi.instance.getSelectedSkinPackageLiveData().observeLifecycleOrForever(v?.findViewTreeLifecycleOwner(), observer)
                }
                override fun onViewDetachedFromWindow(v: View?) {
                    SkinApi.instance.getSelectedSkinPackageLiveData().removeObserver(observer)
                }
            })
        }

        fun onBind() {
            val selectedSkin = SkinApi.instance.getSelectedSkinPackage()
            if (selectedSkin.id == SkinPackage.LIGHT_SKIN_ID) {
                binding.ivThemeIcon.setImageResource(R.drawable.ic_skin_light_mode)
                binding.tvThemeName.text = selectedSkin.name
                binding.tvSkinName.text = "个性装扮"
                binding.themeOptionLayout.setOnClickListener { SkinApi.instance.changeSkin(SkinPackage.DARK_SKIN_ID) }
            } else if (selectedSkin.id == SkinPackage.DARK_SKIN_ID) {
                binding.ivThemeIcon.setImageResource(R.drawable.ic_skin_dark_mode)
                binding.tvThemeName.text = selectedSkin.name
                binding.tvSkinName.text = "个性装扮"
                binding.themeOptionLayout.setOnClickListener { SkinApi.instance.changeSkin(SkinPackage.LIGHT_SKIN_ID) }
            } else {
                binding.tvSkinName.text = "个性装扮-${selectedSkin.name}"
                binding.themeOptionLayout.setOnClickListener { moduleSkinApi.toSkinActivity(binding.root.context) }
            }
            binding.skinOptionLayout.setOnClickListener { moduleSkinApi.toSkinActivity(binding.root.context) }
        }
    }
}