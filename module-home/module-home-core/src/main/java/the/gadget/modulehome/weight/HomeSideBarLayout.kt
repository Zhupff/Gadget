package the.gadget.modulehome.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import the.gadget.modulebase.interfaces.ILayoutRes
import the.gadget.modulebase.weight.recyclerview.BindingRecyclerViewHolder
import the.gadget.modulebase.weight.recyclerview.DiffRecyclerViewAdapter
import the.gadget.modulebase.weight.recyclerview.RecyclerViewAdapter
import the.gadget.modulebase.weight.recyclerview.RecyclerViewHolder
import the.gadget.modulehome.HomeApp
import the.gadget.modulehome.HomeOption
import the.gadget.modulehome.moduleHomeApi
import the.gadget.modulehomecore.R
import the.gadget.modulehomecore.databinding.HomeAppListViewHolderBinding
import the.gadget.modulehomecore.databinding.HomeSideBarLayoutBinding

class HomeSideBarLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ILayoutRes {

    private val binding: HomeSideBarLayoutBinding = DataBindingUtil
        .inflate(LayoutInflater.from(context), getLayoutRes(), this, true)

    private val homeAppObserver = Observer<List<HomeApp>> {
        (binding.rvAppList.adapter as AppListAdapter).submit(it ?: emptyList())
    }

    private val homeOptionObserver = Observer<List<HomeOption>> {
        (binding.rvOptionList.adapter as OptionListAdapter).update(it ?: emptyList())
    }

    init {
        binding.rvAppList.layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
        binding.rvAppList.adapter = AppListAdapter()
        binding.rvAppList.itemAnimator = null

        binding.rvOptionList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvOptionList.adapter = OptionListAdapter()
        binding.rvOptionList.itemAnimator = null
    }

    override fun getLayoutRes(): Int = R.layout.home_side_bar_layout

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        moduleHomeApi.getAllHomeApps().observeForever(homeAppObserver)
        moduleHomeApi.getAllHomeOptions().observeForever(homeOptionObserver)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        moduleHomeApi.getAllHomeApps().removeObserver(homeAppObserver)
        moduleHomeApi.getAllHomeOptions().removeObserver(homeOptionObserver)
    }

    private class AppListAdapter : DiffRecyclerViewAdapter<HomeApp, BindingRecyclerViewHolder<HomeAppListViewHolderBinding>>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingRecyclerViewHolder<HomeAppListViewHolderBinding> =
            BindingRecyclerViewHolder(parent, R.layout.home_app_list_view_holder)

        override fun onBindViewHolder(holder: BindingRecyclerViewHolder<HomeAppListViewHolderBinding>, position: Int) {
            val app = data[position]
            holder.binding.app = app
            holder.binding.root.setOnClickListener { moduleHomeApi.selectHomeApp(app) }
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