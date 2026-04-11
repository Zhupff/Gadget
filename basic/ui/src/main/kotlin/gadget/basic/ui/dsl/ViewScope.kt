package gadget.basic.ui.dsl

import android.view.View
import android.view.ViewGroup
import gadget.basic.annotation.DslScope
import gadget.basic.exception.throws
import gadget.basic.ui.R
import java.lang.ref.WeakReference

class ViewScope<V : View> private constructor(
    view: V,
) : WeakReference<V>(view) {

    companion object {
        fun <V : View> get(view: V): ViewScope<V> {
            return getOrNull(view) ?: ViewScope(view)
        }
        fun <V : View> getOrNull(view: V): ViewScope<V>? {
            val scope = view.getTag(R.id.ViewScope)
            if (scope is ViewScope<*> && scope.get() === view) {
                return scope as ViewScope<V>
            }
            return null
        }
    }

    init {
        if (getOrNull(view) != null) {
            IllegalStateException("Already bind a ViewScope!").throws()
        }
        view.setTag(R.id.ViewScope, this)
    }

    override fun get(): V? {
        val target = super.get()
        if (target == null) {
            // ignore
        }
        return target
    }
}

inline fun <V : View> V.scope(
    params: (@DslScope V).() -> ViewGroup.LayoutParams = { marginLayoutParams() },
    lambda: (@DslScope ViewScope<V>).() -> Unit,
): ViewScope<V> {
    this.layoutParams = params()
    return ViewScope.get(this).apply(lambda)
}
