package the.gadget.component.home

import androidx.annotation.DrawableRes
import the.gadget.fragment.BaseFragment
import the.gadget.weight.recyclerview.RecyclerItemDiffer

abstract class HomeApp(val id: String, val name: String, @DrawableRes val icon: Int) : RecyclerItemDiffer {

    var selected: Boolean = false

    abstract fun newFragment(): BaseFragment

    final override fun getItemSnapshot(): String = id

    final override fun getContentSnapshot(): String = selected.toString()

    final override fun equals(other: Any?): Boolean =
        if (other is HomeApp) other.id == id && other.name == name else false

    final override fun hashCode(): Int = id.hashCode()
}