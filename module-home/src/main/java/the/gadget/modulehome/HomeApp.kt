package the.gadget.modulehome

import androidx.annotation.DrawableRes
import the.gadget.modulebase.fragment.BaseFragment

abstract class HomeApp(val id: String, val name: String, @DrawableRes val icon: Int) {

    var selected: Boolean = false

    abstract fun newFragment(): BaseFragment

    final override fun equals(other: Any?): Boolean =
        if (other is HomeApp) other.id == id && other.name == name else false

    final override fun hashCode(): Int = id.hashCode()
}