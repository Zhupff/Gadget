package the.gadget.moduleskin

import android.view.ViewGroup
import com.google.auto.service.AutoService
import the.gadget.modulebase.weight.recyclerview.BindingRecyclerViewHolder
import the.gadget.modulebase.weight.recyclerview.RecyclerViewHolder
import the.gadget.modulehome.HomeOption
import the.gadget.moduleskincore.R
import the.gadget.moduleskincore.databinding.HomeOptionSkinViewHolderBinding

@AutoService(HomeOption::class)
class HomeOptionSkin : HomeOption(Option.Skin) {

    override fun createViewHolder(container: ViewGroup): RecyclerViewHolder =
        BindingRecyclerViewHolder<HomeOptionSkinViewHolderBinding>(container, R.layout.home_option_skin_view_holder)

    override fun bindViewHolder(viewHolder: RecyclerViewHolder) {
//        (viewHolder.itemView as HomeOptionSkinView).bindView()
    }
}