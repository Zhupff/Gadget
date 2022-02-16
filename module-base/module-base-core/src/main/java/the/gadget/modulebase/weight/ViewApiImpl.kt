package the.gadget.modulebase.weight

import android.view.View
import com.google.auto.service.AutoService

@AutoService(ViewApi::class)
class ViewApiImpl : ViewApi {

    override fun beVisibleOrGone(view: View?, bool: Boolean) {
        switchViewVisibility(view, View.VISIBLE, View.GONE, bool)
    }

    override fun beVisibleOrInvisible(view: View?, bool: Boolean) {
        switchViewVisibility(view, View.VISIBLE, View.INVISIBLE, bool)
    }

    override fun beInvisibleOrGone(view: View?, bool: Boolean) {
        switchViewVisibility(view, View.INVISIBLE, View.GONE, bool)
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