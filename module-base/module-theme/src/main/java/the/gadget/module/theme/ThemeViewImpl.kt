package the.gadget.module.theme

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.findViewTreeLifecycleOwner
import the.gadget.livedata.observeLifecycleOrForever
import the.gadget.theme.Colour
import the.gadget.theme.Palette
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

    override fun onChanged(t: Palette) { actions.values.forEach { if (isAlive) it.run() } }

    private fun active() {
        if (!isAlive) {
            isAlive = true
            ThemeApi.instance.getCurrentTheme().observeLifecycleOrForever(view.findViewTreeLifecycleOwner(), this)
            view.setTag(R.id.theme_view_tag, this)
        }
    }

    override fun release() {
        if (isAlive) {
            isAlive = false
            ThemeApi.instance.getCurrentTheme().removeObserver(this)
            view.setTag(R.id.theme_view_tag, null)
        }
    }

    override fun backgroundColor(colour: Colour) = apply {
        actions["backgroundColor"] = Runnable {
            _backgroundColor(colour)
        }.also { if (isAlive) it.run() }
    }

    override fun textColor(colour: Colour) = apply {
        actions["textColor"] = Runnable {
            _textColor(colour)
        }.also { if (isAlive) it.run() }
    }

    override fun colorFilter(colour: Colour) = apply {
        actions["colorFilter"] = Runnable {
            _colorFilter(colour)
        }.also { if (isAlive) it.run() }
    }



    private fun _backgroundColor(colour: Colour) {
        view.setBackgroundColor(colour.color)
    }

    private fun _textColor(colour: Colour) {
        if (view !is TextView) return
        view.setTextColor(colour.color)
    }

    private fun _colorFilter(colour: Colour) {
        if (view !is ImageView) return
        view.setColorFilter(colour.color)
    }
}