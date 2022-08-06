package the.gadget.module.theme

import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import the.gadget.theme.*
import java.util.concurrent.atomic.AtomicBoolean

class ThemeViewImpl(view: View) : ThemeView(view), View.OnAttachStateChangeListener, Observer<Scheme> {

    private val actions = mutableMapOf<String, Runnable>()

    private val isAttached = AtomicBoolean(false)

    private var currentScheme: Scheme? = null

    private val context: ThemeContext = view.context.let {
        var c = it
        while (c is ContextWrapper) {
            if (c is ThemeContext)
                break
            c = c.baseContext
        }
        c as? ThemeContext ?: throw IllegalStateException("View($view)'s context isn't a ThemeContext.")
    }

    init {
        view.setTag(the.gadget.module.base.R.id.theme_view_tag, this)
        view.addOnAttachStateChangeListener(this)
        onViewAttachedToWindow(view)
    }

    override fun onViewAttachedToWindow(v: View?) {
        if (isAttached.compareAndSet(false, true)) {
            context.getCurrentScheme().observeForever(this)
        }
    }

    override fun onViewDetachedFromWindow(v: View?) {
        if (isAttached.compareAndSet(true, false)) {
            context.getCurrentScheme().removeObserver(this)
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
            val color = currentScheme?.getColour(colour) ?: return@Runnable
            view.setBackgroundColor(color)
        }.apply { run() }
    }

    override fun textColor(colour: Colour) = apply {
        actions["textColor"] = Runnable {
            val color = currentScheme?.getColour(colour) ?: return@Runnable
            when (view) {
                is TextView -> {
                    view.setTextColor(color)
                }
            }
        }.apply { run() }
    }

    override fun hintColor(colour: Colour) = apply {
        actions["hintColor"] = Runnable {
            val color = currentScheme?.getColour(colour) ?: return@Runnable
            when (view) {
                is EditText -> {
                    view.setHintTextColor(color)
                }
            }
        }.apply { run() }
    }

    override fun foregroundTint(colour: Colour) = apply {
        actions["foregroundTint"] = Runnable {
            val color = currentScheme?.getColour(colour) ?: return@Runnable
            when (view) {
                is ImageView -> {
                    view.setColorFilter(color)
                }
            }
        }.apply { run() }
    }

    override fun backgroundTint(colour: Colour) = apply {
        actions["backgroundTint"] = Runnable {
            val color = currentScheme?.getColour(colour) ?: return@Runnable
            view.backgroundTintList = ColorStateList.valueOf(color)
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