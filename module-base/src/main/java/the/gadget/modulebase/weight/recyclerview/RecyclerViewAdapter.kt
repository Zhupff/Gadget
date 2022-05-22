package the.gadget.modulebase.weight.recyclerview

import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewAdapter<T, V: RecyclerViewHolder> : RecyclerView.Adapter<V>() {

    protected open val data: ArrayList<T> = ArrayList()

    override fun getItemCount(): Int = data.size

    open fun update(newData: List<T>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}