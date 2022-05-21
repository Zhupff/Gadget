package the.gadget.modulebase.skin

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.lifecycle.Observer
import the.gadget.modulebase.R
import the.gadget.modulebase.weight.listener.ViewOnAttachStateChangeListener

class SkinView(val view: View) : Observer<SkinPackage> {
    companion object {
        fun get(view: View): SkinView? = view.getTag(R.id.skin_view_tag) as? SkinView
    }

    private val actions = mutableMapOf<String, Runnable>()

    private var isAlive = true

    init {
        SkinApi.instance.getSelectedSkinPackageLiveData().observeForever(this)
        view.addOnAttachStateChangeListener(object : ViewOnAttachStateChangeListener() {
            override fun onViewDetachedFromWindow(v: View?) {
                super.onViewDetachedFromWindow(v)
                release()
            }
        })
        view.setTag(R.id.skin_view_tag, this)
    }

    override fun onChanged(skinPackage: SkinPackage) {
        actions.values.forEach { if (isAlive) it.run() }
    }

    @MainThread
    fun release() {
        isAlive = false
        SkinApi.instance.getSelectedSkinPackageLiveData().removeObserver(this@SkinView)
        view.setTag(R.id.skin_view_tag, null)
    }


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