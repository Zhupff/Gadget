package the.gadget.fragment

import androidx.fragment.app.FragmentManager
import the.gadget.api.globalApi

interface FragmentApi {
    companion object : FragmentApi by FragmentApi::class.globalApi()

    fun isAlive(fragment: BaseFragment?): Boolean

    fun isAlive(dialogFragment: BaseDialogFragment?): Boolean

    fun showDialogFragment(fragmentManager: FragmentManager, dialogFragment: BaseDialogFragment, tag: String = dialogFragment.javaClass.simpleName)
}

fun BaseFragment?.isAlive(): Boolean = FragmentApi.isAlive(this)

fun BaseDialogFragment?.isAlive(): Boolean = FragmentApi.isAlive(this)