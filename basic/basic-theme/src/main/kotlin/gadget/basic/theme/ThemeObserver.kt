package gadget.basic.theme

import android.app.Activity
import android.content.ContextWrapper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import gadget.basic.exception.throws
import java.lang.ref.WeakReference
import java.util.LinkedList
import kotlin.also
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.set

internal class ThemeObserver(
    view: View,
    private val attribute2resource: Map<Theme.Attribute, Theme.Resource>,
    private var flags: Int = 0,
) : WeakReference<View>(view), View.OnAttachStateChangeListener, LifecycleOwner, Observer<Theme> {

    companion object {
        const val FLAG_TRACED = 1
        const val FLAG_MUTABLE = 2
    }

    override val lifecycle: LifecycleRegistry = LifecycleRegistry(this)

    private var observable: ThemeObservable? = null

    private var currentThemeID: String = ""

    init {
        if (view.getTag(R.id.ThemeObserver) != null) {
            IllegalStateException("Already bind ThemeObserver!").throws()
        } else {
            view.setTag(R.id.ThemeObserver, this)
        }
        lifecycle.currentState = Lifecycle.State.CREATED
        view.addOnAttachStateChangeListener(this)
        if (view.isAttachedToWindow) {
            onViewAttachedToWindow(view)
        }
    }

    override fun get(): View? {
        val target = super.get()
        if (target == null) {
            lifecycle.currentState = Lifecycle.State.DESTROYED
            observable = null
        }
        return target
    }

    /**
     * 当View attached的时候将生命周期置为RESUMED以获取主题资源。
     */
    override fun onViewAttachedToWindow(v: View) {
        val target = get()
        if (target == null || target != v) {
            return
        }
        lifecycle.currentState = Lifecycle.State.RESUMED
        if (flags and FLAG_TRACED == 0) { // 首次attach。
            trace(target).also {
                flags = flags or FLAG_TRACED
                observable = it
            }.subscribe().observe(this, this)
        } else {
            if (flags and FLAG_MUTABLE != 0) {
                // 如果是可变的，那么每次attach的时候都需要重新trace一下对应的主题源。
                trace(target).also {
                    observable = it
                }.subscribe().observe(this, this)
            }
        }
    }

    /**
     * 当View detached的时候：
     * 1.如果是FLAG_MUTABLE的，那么需要切换生命周期为DESTROYED；
     * 2.如果不是FLAG_MUTABLE，那么只需切换生命周期为PAUSED即可；
     */
    override fun onViewDetachedFromWindow(v: View) {
        val target = get()
        if (target == null || target != v) {
            return
        }
        if (flags and FLAG_MUTABLE != 0) {
            // 如果是可变的，每次detach的时候将生命周期设为Destroyed，这样Observable就会自动清理该Observer；
            // 在下次attach的时候再重新trace对应的Observable并observe。
            lifecycle.currentState = Lifecycle.State.DESTROYED
            observable = null
        } else {
            // 如果不是可变的，那只需要将生命周期设为CREATED（等价于paused）。
            lifecycle.currentState = Lifecycle.State.CREATED
        }
    }

    override fun onChanged(value: Theme) {
        val target = get()
        if (target == null || currentThemeID == value.id) {
            return
        }
        currentThemeID = value.id
        attribute2resource.forEach { (attribute, resource) ->
            attribute.apply(target, value, resource)
        }
    }

    /**
     * 按照【view->fragment->activity->application】的优先级，逐层往上溯源最近的一个[ThemeObservable]。
     */
    private fun trace(target: View): ThemeObservable {
        if (target is ThemeObservable) {
            return target
        }
        var context = target.context
        while (context is ContextWrapper) {
            if (context is Activity) {
                break
            }
            context = context.baseContext
        }
        if (context is FragmentActivity) {
            val map = kotlin.collections.HashMap<View, Fragment>()
            val queue = LinkedList<Fragment>(context.supportFragmentManager.fragments)
            while (queue.isNotEmpty()) {
                val fragment = queue.pop() ?: continue
                val view = fragment.view ?: continue
                map[view] = fragment
                queue.addAll(fragment.childFragmentManager.fragments)
            }
            val root = context.findViewById<View>(android.R.id.content)
            var view = target
            while (view !== root) {
                val fragment = map[view]
                if (fragment != null && fragment is ThemeObservable) {
                    return fragment
                }
                val parent = view.parent
                if (parent is View) {
                    val observer = parent.getTag(R.id.ThemeObserver)
                    if (observer is ThemeObserver) {
                        // 首次attach且最近的observable是View的时候，需要处理些flags相关的内容。
                        if (flags and FLAG_TRACED == 0) {
                            // 如果父View是可变的，那意味着当前View也是可变的。
                            this.flags = this.flags or (observer.flags and FLAG_MUTABLE)
                        }
                        return observer.observable!!
                    }
                } else if (parent is ThemeObservable) {
                    return parent
                }
                view = parent as? View ?: break
            }
        }
        if (context is ThemeObservable) {
            return context
        }
        return context.applicationContext as? ThemeObservable ?: IllegalStateException("ThemeObservable not found!").throws()
    }
}