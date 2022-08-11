package the.gadget.weight

import android.view.View
import the.gadget.api.globalApi

interface ViewApi {
    companion object : ViewApi by ViewApi::class.globalApi()

    fun beVisible(view: View?)

    fun beInvisible(view: View?)

    fun beGone(view: View?)

    fun beVisibleOrGone(view: View?, bool: Boolean)

    fun beVisibleOrInvisible(view: View?, bool: Boolean)

    fun beInvisibleOrGone(view: View?, bool: Boolean)

    fun postAutoRemove(view: View?, runnable: Runnable)
}


fun View?.beVisible() { ViewApi.beVisible(this) }
fun View?.beInvisible() { ViewApi.beInvisible(this) }
fun View?.beGone() { ViewApi.beGone(this) }
fun View?.beVisibleOrGone(bool: Boolean) { ViewApi.beVisibleOrGone(this, bool) }
fun View?.beVisibleOrInvisible(bool: Boolean) { ViewApi.beVisibleOrInvisible(this, bool) }
fun View?.beInvisibleOrGone(bool: Boolean) { ViewApi.beInvisibleOrGone(this, bool) }

fun View?.postAutoRemove(runnable: Runnable) { ViewApi.postAutoRemove(this, runnable) }