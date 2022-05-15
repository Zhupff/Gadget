package the.gadget.modulebase.fragment

import androidx.fragment.app.FragmentManager
import the.gadget.modulebase.api.apiInstance

interface FragmentApi {
    companion object {
        val instance: FragmentApi by lazy { apiInstance(FragmentApi::class.java) }
    }

    fun isAlive(fragment: BaseFragment?): Boolean

    fun isAlive(dialogFragment: BaseDialogFragment?): Boolean

    fun showDialogFragment(fragmentManager: FragmentManager, dialogFragment: BaseDialogFragment, tag: String = dialogFragment.javaClass.simpleName)
}

fun BaseFragment?.isAlive(): Boolean = FragmentApi.instance.isAlive(this)

fun BaseDialogFragment?.isAlive(): Boolean = FragmentApi.instance.isAlive(this)