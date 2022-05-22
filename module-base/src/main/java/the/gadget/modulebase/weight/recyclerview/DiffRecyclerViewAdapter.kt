package the.gadget.modulebase.weight.recyclerview

import androidx.recyclerview.widget.DiffUtil

abstract class DiffRecyclerViewAdapter<T : RecyclerItemDiffer, V : RecyclerViewHolder> : RecyclerViewAdapter<T, V>() {

    protected var snapshot: List<Pair<String, String>> = emptyList()

    open fun submit(newData: List<T>) {
        val newSnapshot = newData.map { it.getItemSnapshot() to it.getContentSnapshot() }
        val differ = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = data.size
            override fun getNewListSize(): Int = newData.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                newSnapshot[newItemPosition].first == snapshot[oldItemPosition].first
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                newSnapshot[newItemPosition].second == snapshot[oldItemPosition].second
        })
        data.clear()
        data.addAll(newData)
        snapshot = newSnapshot
        differ.dispatchUpdatesTo(this)
    }
}