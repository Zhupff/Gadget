package the.gadget.modulebase.weight.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BindingRecyclerViewHolder<V : ViewDataBinding> : RecyclerViewHolder {

    val binding: V

    constructor(view: View) : super(view) {
        binding = DataBindingUtil.bind(this.itemView)!!
    }

    constructor(container: ViewGroup, @LayoutRes id: Int) : super(container, id) {
        binding = DataBindingUtil.bind(this.itemView)!!
    }

    constructor(binding: V) : super(binding.root) {
        this.binding = binding
    }
}