package the.gadget.module.common

import android.view.View
import com.google.auto.service.AutoService
import the.gadget.weight.ViewApi
import the.gadget.weight.listener.ViewOnAttachStateChangeListener

@AutoService(ViewApi::class)
class ViewApiImpl : ViewApi {

    override fun beVisible(view: View?) {
        switchViewVisibility(view, View.VISIBLE)
    }

    override fun beInvisible(view: View?) {
        switchViewVisibility(view, View.INVISIBLE)
    }

    override fun beGone(view: View?) {
        switchViewVisibility(view, View.GONE)
    }

    override fun beVisibleOrGone(view: View?, bool: Boolean) {
        switchViewVisibility(view, View.VISIBLE, View.GONE, bool)
    }

    override fun beVisibleOrInvisible(view: View?, bool: Boolean) {
        switchViewVisibility(view, View.VISIBLE, View.INVISIBLE, bool)
    }

    override fun beInvisibleOrGone(view: View?, bool: Boolean) {
        switchViewVisibility(view, View.INVISIBLE, View.GONE, bool)
    }

    override fun postAutoRemove(view: View?, runnable: Runnable) {
        view?.post(runnable)
        view?.addOnAttachStateChangeListener(object : ViewOnAttachStateChangeListener() {
            override fun onViewDetachedFromWindow(v: View?) {
                super.onViewDetachedFromWindow(v)
                v?.removeCallbacks(runnable)
                v?.removeOnAttachStateChangeListener(this)
            }
        })
    }

    private fun switchViewVisibility(view: View?, toBe: Int) {
        if (view?.visibility == toBe) return
        view?.visibility = toBe
    }

    private fun switchViewVisibility(view: View?, toBe: Int, notToBe: Int, bool: Boolean) {
        view?.let {
            if (bool && it.visibility != toBe) {
                it.visibility = toBe
            } else if (!bool && it.visibility != notToBe) {
                it.visibility = notToBe
            }
        }
    }
}