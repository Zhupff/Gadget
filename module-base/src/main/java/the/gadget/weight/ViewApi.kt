package the.gadget.weight

import android.view.View
import the.gadget.api.apiInstance

interface ViewApi {
    companion object {
        @JvmStatic
        val instance: ViewApi by lazy { apiInstance(ViewApi::class.java) }
    }

    fun beVisibleOrGone(view: View?, bool: Boolean)

    fun beVisibleOrInvisible(view: View?, bool: Boolean)

    fun beInvisibleOrGone(view: View?, bool: Boolean)
}


fun View?.beVisibleOrGone(bool: Boolean) { ViewApi.instance.beVisibleOrGone(this, bool) }
fun View?.beVisibleOrInvisible(bool: Boolean) { ViewApi.instance.beVisibleOrInvisible(this, bool) }
fun View?.beInvisibleOrGone(bool: Boolean) { ViewApi.instance.beInvisibleOrGone(this, bool) }