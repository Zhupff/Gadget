package the.gadget.modulebase.weight.recyclerview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class DiffFragmentStateAdapter<T : RecyclerItemDiffer> : FragmentStateAdapter {

    constructor(activity: FragmentActivity): super(activity)
    constructor(fragment: Fragment): super(fragment)
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle): super(fragmentManager, lifecycle)

    protected open var data: List<T> = emptyList()
    protected var snapshot: List<Pair<String, String>> = emptyList()

    open fun submit(newData: List<T>) {
        val newSnapshot = newData.map { it.getItemSnapshot() to it.getContentSnapshot() }
        val differ = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = snapshot.size
            override fun getNewListSize(): Int = newSnapshot.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                newSnapshot[newItemPosition].first == snapshot[oldItemPosition].first
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                newSnapshot[newItemPosition].second == snapshot[oldItemPosition].second
        })
        data = newData
        snapshot = newSnapshot
        differ.dispatchUpdatesTo(this)
    }
}