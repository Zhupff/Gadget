package the.gadget.module.theme

import android.content.res.ColorStateList
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import the.gadget.theme.Colour
import the.gadget.theme.Scheme
import the.gadget.theme.ThemeApi
import the.gadget.theme.ThemeView
import java.util.concurrent.atomic.AtomicBoolean

internal class ThemeViewImpl(view: View) : ThemeView(view), View.OnAttachStateChangeListener, Observer<Scheme> {

    private val actions = mutableMapOf<String, Runnable>()

    private val isAttached = AtomicBoolean(false)

    private var currentScheme: Scheme? = null

    init {
        view.setTag(the.gadget.module.base.R.id.theme_view_tag, this)
        view.addOnAttachStateChangeListener(this)
        onViewAttachedToWindow(view)
    }

    override fun onViewAttachedToWindow(v: View?) {
        if (isAttached.compareAndSet(false, true)) {
            ThemeApi.instance.getCurrentScheme().observeForever(this)
        }
    }

    override fun onViewDetachedFromWindow(v: View?) {
        if (isAttached.compareAndSet(true, false)) {
            ThemeApi.instance.getCurrentScheme().removeObserver(this)
        }
    }

    override fun onChanged(scheme: Scheme) {
        if (scheme != currentScheme) {
            currentScheme = scheme
            actions.values.forEach {
                it.run()
            }
        }
    }

    override fun release() {
        view.setTag(the.gadget.module.base.R.id.theme_view_tag, null)
        view.removeOnAttachStateChangeListener(this)
        onViewDetachedFromWindow(view)
    }

    override fun backgroundColor(colour: Colour) = apply {
        actions["backgroundColor"] = Runnable {
            view.setBackgroundColor(colour.color)
        }.apply { run() }
    }

    override fun textColor(colour: Colour) = apply {
        actions["textColor"] = Runnable {
            when (view) {
                is TextView -> {
                    view.setTextColor(colour.color)
                }
            }
        }.apply { run() }
    }

    override fun hintColor(colour: Colour) = apply {
        actions["hintColor"] = Runnable {
            when (view) {
                is EditText -> {
                    view.setHintTextColor(colour.color)
                }
            }
        }.apply { run() }
    }

    override fun foregroundTint(colour: Colour) = apply {
        actions["foregroundTint"] = Runnable {
            when (view) {
                is ImageView -> {
                    view.setColorFilter(colour.color)
                }
            }
        }.apply { run() }
    }

    override fun backgroundTint(colour: Colour) = apply {
        actions["backgroundTint"] = Runnable {
            view.backgroundTintList = ColorStateList.valueOf(colour.color)
        }.apply { run() }
    }

    override fun wallpaper(): ThemeView = apply {
        actions["wallpaper"] = Runnable {
            when (view) {
                is ImageView -> {
                    val wallpaper = ThemeApi.instance.getCurrentScheme().value?.wallpaper
                    view.setImageBitmap(wallpaper)
                }
            }
        }.apply { run() }
    }
}