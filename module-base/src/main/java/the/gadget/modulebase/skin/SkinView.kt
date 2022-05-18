package the.gadget.modulebase.skin

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread

class SkinView(val view: View) {

    private val actions = mutableMapOf<String, Runnable>()

    private var isAlive = true

    @MainThread
    fun withSkinBackgroundColor(id: Int) = apply {
        actions["withSkinBackgroundColor"] = Runnable {
            skinBackgroundColor(view, id)
        }.also { if (isAlive) it.run() }
    }

    @MainThread
    fun withSkinTextColor(id: Int) = apply {
        actions["withSkinTextColor"] = Runnable {
            skinTextColor(view as TextView, id)
        }.also { if (isAlive) it.run() }
    }

    @MainThread
    fun withSkinColorFilter(id: Int) = apply {
        actions["withSkinColorFilter"] = Runnable {
            skinColorFilter(view as ImageView, id)
        }.also { if (isAlive) it.run() }
    }

    @MainThread
    fun withSkinDrawableRes(id: Int) = apply {
        actions["withSkinDrawableRes"] = Runnable {
            skinDrawableRes(view as ImageView, id)
        }.also { if (isAlive) it.run() }
    }

    @MainThread
    fun notifyChange() {
        actions.values.forEach { if (isAlive) it.run() }
    }

    @MainThread
    fun release() {
        isAlive = false
    }


    private fun skinBackgroundColor(view: View, @ColorInt id: Int) {
        if (id == 0) return
        view.setBackgroundColor(SkinApi.instance.getColorInt(id))
    }

    private fun skinTextColor(view: TextView, @ColorRes id: Int) {
        if (id == 0) return
        val colorStateList = SkinApi.instance.getColorStateList(id)
        if (colorStateList != null) {
            view.setTextColor(colorStateList)
        } else {
            view.setTextColor(SkinApi.instance.getColorInt(id))
        }
    }

    private fun skinColorFilter(view: ImageView, @ColorRes id: Int) {
        if (id == 0) return
        view.setColorFilter(SkinApi.instance.getColorInt(id))
    }

    private fun skinDrawableRes(view: ImageView, @DrawableRes id: Int) {
        if (id == 0) return
        view.setImageDrawable(SkinApi.instance.getDrawable(id))
    }
}