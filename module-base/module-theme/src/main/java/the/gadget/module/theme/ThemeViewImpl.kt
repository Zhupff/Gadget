package the.gadget.module.theme

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.findViewTreeLifecycleOwner
import the.gadget.livedata.observeLifecycleOrForever
import the.gadget.theme.Colour
import the.gadget.theme.Scheme
import the.gadget.theme.ThemeApi
import the.gadget.theme.ThemeView
import the.gadget.weight.listener.ViewOnAttachStateChangeListener

internal class ThemeViewImpl(view: View) : ThemeView(view) {

    private val actions = mutableMapOf<String, Runnable>()

    private var isAlive = false

    init {
        view.addOnAttachStateChangeListener(object : ViewOnAttachStateChangeListener() {
            override fun onViewAttachedToWindow(v: View?) {
                super.onViewAttachedToWindow(v)
                active()
            }
            override fun onViewDetachedFromWindow(v: View?) {
                super.onViewDetachedFromWindow(v)
                release()
            }
        })
        active()
    }

    override fun onChanged(t: Scheme) { actions.values.forEach { if (isAlive) it.run() } }

    private fun active() {
        if (!isAlive) {
            isAlive = true
            ThemeApi.instance.getCurrentScheme().observeLifecycleOrForever(view.findViewTreeLifecycleOwner(), this)
            view.setTag(R.id.theme_view_tag, this)
        }
    }

    override fun release() {
        if (isAlive) {
            isAlive = false
            ThemeApi.instance.getCurrentScheme().removeObserver(this)
            view.setTag(R.id.theme_view_tag, null)
        }
    }

    override fun backgroundColor(colour: Colour) = apply {
        actions["backgroundColor"] = Runnable {
            backgroundColorAction(colour)
        }.also { if (isAlive) it.run() }
    }

    override fun textColor(colour: Colour) = apply {
        actions["textColor"] = Runnable {
            textColorAction(colour)
        }.also { if (isAlive) it.run() }
    }

    override fun foregroundTint(colour: Colour) = apply {
        actions["foregroundTint"] = Runnable {
            foregroundTintAction(colour)
        }.also { if (isAlive) it.run() }
    }

    override fun backgroundTint(colour: Colour) = apply {
        actions["backgroundTint"] = Runnable {
            backgroundTintAction(colour)
        }.also { if (isAlive) it.run() }
    }

    override fun wallpaper(): ThemeView = apply {
        actions["wallpaper"] = Runnable {
            wallpaperAction()
        }.also { if (isAlive) it.run() }
    }


    private fun backgroundColorAction(colour: Colour) {
        view.setBackgroundColor(colour.color)
    }

    private fun textColorAction(colour: Colour) {
        if (view !is TextView) return
        view.setTextColor(colour.color)
    }

    private fun foregroundTintAction(colour: Colour) {
        if (view !is ImageView) return
        view.setColorFilter(colour.color)
    }

    private fun backgroundTintAction(colour: Colour) {
        view.backgroundTintList = ColorStateList.valueOf(colour.color)
    }

    private fun wallpaperAction() {
        if (view !is ImageView) return
        val wallpaper = ThemeApi.instance.getCurrentScheme().value?.wallpaper
        view.setImageBitmap(wallpaper)
    }
}