package the.gadget.moduleskin

import android.view.ViewGroup
import com.google.auto.service.AutoService
import the.gadget.modulebase.skin.SkinApi
import the.gadget.modulebase.skin.SkinPackage
import the.gadget.modulebase.weight.recyclerview.BindingRecyclerViewHolder
import the.gadget.modulebase.weight.recyclerview.RecyclerViewHolder
import the.gadget.modulehome.HomeOption
import the.gadget.moduleskincore.R
import the.gadget.moduleskincore.databinding.HomeOptionSkinViewHolderBinding

@AutoService(HomeOption::class)
class HomeOptionSkin : HomeOption(Option.Skin) {

    private var viewHolder: RecyclerViewHolder? = null

    init {
        SkinApi.instance.getSelectedSkinPackageLiveData().observeForever { bindView() }
    }

    override fun createViewHolder(container: ViewGroup): RecyclerViewHolder =
        BindingRecyclerViewHolder<HomeOptionSkinViewHolderBinding>(container, R.layout.home_option_skin_view_holder).also { viewHolder = it }

    override fun bindViewHolder(viewHolder: RecyclerViewHolder) {
        if (viewHolder != this.viewHolder) {
            this.viewHolder = viewHolder
        }
        bindView()
    }

    private fun bindView() {
        val binding = (this.viewHolder as? BindingRecyclerViewHolder<HomeOptionSkinViewHolderBinding>)?.binding ?: return
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
            binding.themeOptionLayout.setOnClickListener {  }
        }
        binding.skinOptionLayout.setOnClickListener {  }
    }
}