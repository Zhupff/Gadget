package the.gadget.modulehome

import android.view.ViewGroup
import the.gadget.modulebase.weight.recyclerview.RecyclerViewHolder

abstract class HomeOption(val option: Option) {
    enum class Option {
        Skin,
        About,
    }

    abstract fun createViewHolder(container: ViewGroup): RecyclerViewHolder

    abstract fun bindViewHolder(viewHolder: RecyclerViewHolder)
}