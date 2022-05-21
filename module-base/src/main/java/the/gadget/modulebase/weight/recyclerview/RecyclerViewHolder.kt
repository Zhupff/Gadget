package the.gadget.modulebase.weight.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

open class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    constructor(container: ViewGroup, @LayoutRes id: Int) :
        this(LayoutInflater.from(container.context).inflate(id, container, false))
}