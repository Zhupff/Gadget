package gadget.basic.theme

import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import gadget.basic.exception.throws
import java.lang.ref.WeakReference
import java.util.LinkedList

internal class ThemeSubscriber private constructor(
    view: View,
) : WeakReference<View>(view),
    View.OnAttachStateChangeListener,
    Observer<ThemeScheme> {

    companion object {
        fun get(view: View): ThemeSubscriber {
            return getOrNull(view) ?: ThemeSubscriber(view)
        }
        fun getOrNull(view: View): ThemeSubscriber? {
            return view.getTag(R.id.ThemeSubscriber) as? ThemeSubscriber
        }
        private val NO_ACTION: (ThemeScheme) -> Unit = {}
    }

    init {
        view.setTag(R.id.ThemeSubscriber, this)
        view.addOnAttachStateChangeListener(this)
        if (view.isAttachedToWindow) {
            onViewAttachedToWindow(view)
        }
    }

    private var observable: LiveData<out ThemeScheme>? = null

    private var current: ThemeScheme? = null

    private var action: (ThemeScheme) -> Unit = NO_ACTION

    fun subscribe(
        observable: LiveData<out ThemeScheme>? = this.observable,
        action: (ThemeScheme) -> Unit = {},
    ) {
        this.action = action
        if (this.observable !== observable) {
            this.observable?.removeObserver(this)
            this.observable = observable
            this.observable?.observeForever(this) ?: this.current?.let(this.action)
        } else {
            this.current?.let(this.action)
        }
    }

    override fun onViewAttachedToWindow(view: View) {
        val target = get()
        if (target == null || target != view) {
            return
        }
        if (this.observable != null) {
            return
        }
        var scheme: ThemeScheme? = null
        var parent = target.parent
        while (parent is View) {
            scheme = getOrNull(parent)?.current
            if (scheme != null) {
                break
            } else {
                parent = parent.parent
            }
        }
        if (scheme != null) {
            onChanged(scheme)
        } else {
            IllegalStateException("ThemeScheme not found!").throws()
        }
    }

    override fun onViewDetachedFromWindow(view: View) {}

    override fun onChanged(value: ThemeScheme) {
        if (this.current === value || get() == null) {
            return
        }
        this.current = value
        this.current?.let(this.action)
        if (observable != null) {
            val queue = LinkedList<ThemeSubscriber>()
            (get() as? ViewGroup)?.children?.forEach { child ->
                getOrNull(child)?.let(queue::offer)
            }
            while (queue.isNotEmpty()) {
                val subscriber = queue.poll() ?: continue
                if (subscriber.observable != null) {
                    continue
                }
                subscriber.onChanged(value)
                val view = subscriber.get() ?: continue
                if (view is ViewGroup) {
                    view.children.forEach { child ->
                        getOrNull(child)?.let(queue::offer)
                    }
                }
            }
        }
    }

    override fun get(): View? {
        val target = super.get()
        if (target == null) {
            if (observable != null) {
                Looper.getMainLooper().queue.addIdleHandler {
                    observable?.removeObserver(this)
                    observable = null
                    false
                }
            }
            action = NO_ACTION
            current = null
        }
        return target
    }
}