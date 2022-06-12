package the.gadget.component.setting

import android.view.ViewGroup
import com.google.auto.service.AutoService
import the.gadget.api.singleToastS
import the.gadget.component.home.HomeOption
import the.gadget.component.setting.databinding.HomeOptionAboutViewHolderBinding
import the.gadget.weight.recyclerview.BindingRecyclerViewHolder
import the.gadget.weight.recyclerview.RecyclerViewHolder

@AutoService(HomeOption::class)
class HomeOptionAbout : HomeOption(Option.About) {
    override fun createViewHolder(container: ViewGroup): RecyclerViewHolder =
        BindingRecyclerViewHolder<HomeOptionAboutViewHolderBinding>(container, R.layout.home_option_about_view_holder)

    override fun bindViewHolder(viewHolder: RecyclerViewHolder) {
        viewHolder.itemView.setOnClickListener { "该功能暂未支持，尽情期待！".singleToastS() }
    }
}