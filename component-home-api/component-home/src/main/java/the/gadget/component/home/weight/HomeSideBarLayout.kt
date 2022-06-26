package the.gadget.component.home.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import the.gadget.component.home.HomeApp
import the.gadget.component.home.HomeOption
import the.gadget.component.home.R
import the.gadget.component.home.componentHomeApi
import the.gadget.component.home.databinding.HomeAppListViewHolderBinding
import the.gadget.component.home.databinding.HomeSideBarLayoutBinding
import the.gadget.interfaces.ILayoutRes
import the.gadget.livedata.observe
import the.gadget.user.UserApi
import the.gadget.weight.recyclerview.BindingRecyclerViewHolder
import the.gadget.weight.recyclerview.DiffRecyclerViewAdapter
import the.gadget.weight.recyclerview.RecyclerViewAdapter
import the.gadget.weight.recyclerview.RecyclerViewHolder

class HomeSideBarLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ILayoutRes {

    private val binding: HomeSideBarLayoutBinding = DataBindingUtil
        .inflate(LayoutInflater.from(context), getLayoutRes(), this, true)

    init {
        binding.ivAvatar.setOnClickListener { UserApi.instance.showUserInfoPopupDialog(context) }

        binding.rvAppList.layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
        binding.rvAppList.adapter = AppListAdapter()
        binding.rvAppList.itemAnimator = null

        binding.rvOptionList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvOptionList.adapter = OptionListAdapter()
        binding.rvOptionList.itemAnimator = null

        UserApi.instance.getCurrentUser().observe(this) { binding.user = it }
        componentHomeApi.getAllHomeApps().observe(this) {
            (binding.rvAppList.adapter as AppListAdapter).submit(it ?: emptyList())
        }
        componentHomeApi.getAllHomeOptions().observe(this) {
            (binding.rvOptionList.adapter as OptionListAdapter).update(it ?: emptyList())
        }
    }

    override fun getLayoutRes(): Int = R.layout.home_side_bar_layout

    private class AppListAdapter : DiffRecyclerViewAdapter<HomeApp, BindingRecyclerViewHolder<HomeAppListViewHolderBinding>>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingRecyclerViewHolder<HomeAppListViewHolderBinding> =
            BindingRecyclerViewHolder(parent, R.layout.home_app_list_view_holder)

        override fun onBindViewHolder(holder: BindingRecyclerViewHolder<HomeAppListViewHolderBinding>, position: Int) {
            val app = data[position]
            holder.binding.app = app
            holder.binding.root.setOnClickListener { componentHomeApi.selectHomeApp(app) }
        }
    }

    private class OptionListAdapter : RecyclerViewAdapter<HomeOption, RecyclerViewHolder>() {

        override fun getItemViewType(position: Int): Int = position

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder =
            data[viewType].createViewHolder(parent)

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            data[position].bindViewHolder(holder)
        }
    }
}