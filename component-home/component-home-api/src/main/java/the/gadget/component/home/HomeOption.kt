package the.gadget.component.home

import android.view.ViewGroup
import the.gadget.weight.recyclerview.RecyclerViewHolder

abstract class HomeOption(val option: Option) {
    enum class Option {
        Theme,
        About,
    }

    abstract fun createViewHolder(container: ViewGroup): RecyclerViewHolder

    abstract fun bindViewHolder(viewHolder: RecyclerViewHolder)
}