package the.gadget.weight.recyclerview

import androidx.recyclerview.widget.DiffUtil

abstract class DiffRecyclerViewAdapter<T : RecyclerItemDiffer, V : RecyclerViewHolder> : RecyclerViewAdapter<T, V>() {

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