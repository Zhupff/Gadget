package the.gadget.modulebase.weight.recyclerview

import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewAdapter<T, V: RecyclerViewHolder> : RecyclerView.Adapter<V>() {

    protected open var data: List<T> = emptyList()

    override fun getItemCount(): Int = data.size

    open fun update(newData: List<T>) {
        data = newData
        notifyDataSetChanged()
    }
}