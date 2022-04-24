package the.gadget.modulebase.skin

import android.view.View
import android.widget.TextView
import androidx.annotation.MainThread

class SkinView(val view: View) {

    private val actions = mutableListOf<Runnable>()

    @MainThread
    fun skinBackgroundColor(id: Int) = apply {
        actions.add(Runnable {
            SkinViewDataBindingAdapter.skinBackgroundColor(view, id)
        }.also { it.run() })
    }

    @MainThread
    fun skinTextColor(id: Int) = apply {
        actions.add(Runnable {
            SkinViewDataBindingAdapter.skinTextColor(view as TextView, id)
        }.also { it.run() })
    }

    @MainThread
    fun notifyChange() {
        actions.forEach { it.run() }
    }
}