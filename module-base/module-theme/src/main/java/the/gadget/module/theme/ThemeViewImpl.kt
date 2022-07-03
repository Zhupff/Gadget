package the.gadget.module.theme

import android.content.res.ColorStateList
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.findViewTreeLifecycleOwner
import the.gadget.livedata.observeLifecycleOrForever
import the.gadget.theme.Colour
import the.gadget.theme.Scheme
import the.gadget.theme.ThemeApi
import the.gadget.theme.ThemeView

internal class ThemeViewImpl(view: View) : ThemeView(view), View.OnAttachStateChangeListener {

    private inner class Action(val action: Runnable) {
        init { if (isAttached) action.run() }
    }

    private val actions = mutableMapOf<String, Action>()

    private var isAttached = false

    init {
        view.addOnAttachStateChangeListener(this)
        if (view.isAttachedToWindow) onViewAttachedToWindow(view)
    }

    override fun onViewAttachedToWindow(v: View?) {
        if (!isAttached) {
            isAttached = true
            ThemeApi.instance.getCurrentScheme().observeLifecycleOrForever(view.findViewTreeLifecycleOwner(), this)
            view.setTag(the.gadget.module.base.R.id.theme_view_tag, this)
        }
    }

    override fun onViewDetachedFromWindow(v: View?) {
        if (isAttached) {
            isAttached = false
            ThemeApi.instance.getCurrentScheme().removeObserver(this)
            view.setTag(the.gadget.module.base.R.id.theme_view_tag, null)
        }
    }

    override fun onChanged(scheme: Scheme) { actions.values.forEach { it.action.run() } }

    override fun release() {
        view.removeOnAttachStateChangeListener(this)
        onViewDetachedFromWindow(view)
    }

    override fun backgroundColor(colour: Colour) = apply {
        actions["backgroundColor"] = Action {
            view.setBackgroundColor(colour.color)
        }
    }

    override fun textColor(colour: Colour) = apply {
        actions["textColor"] = Action {
            if (view is TextView) {
                view.setTextColor(colour.color)
            }
        }
    }

    override fun hintColor(colour: Colour) = apply {
        actions["hintColor"] = Action {
            if (view is EditText) {
                view.setHintTextColor(colour.color)
            }
        }
    }

    override fun foregroundTint(colour: Colour) = apply {
        actions["foregroundTint"] = Action {
            if (view is ImageView) {
                view.setColorFilter(colour.color)
            }
        }
    }

    override fun backgroundTint(colour: Colour) = apply {
        actions["backgroundTint"] = Action {
            view.backgroundTintList = ColorStateList.valueOf(colour.color)
        }
    }

    override fun wallpaper(): ThemeView = apply {
        actions["wallpaper"] = Action {
            if (view is ImageView) {
                val wallpaper = ThemeApi.instance.getCurrentScheme().value?.wallpaper
                view.setImageBitmap(wallpaper)
            }
        }
    }
}