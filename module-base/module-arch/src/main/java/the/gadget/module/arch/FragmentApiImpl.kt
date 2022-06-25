package the.gadget.module.arch

import androidx.fragment.app.FragmentManager
import com.google.auto.service.AutoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import the.gadget.fragment.BaseDialogFragment
import the.gadget.fragment.BaseFragment
import the.gadget.fragment.FragmentApi

@AutoService(FragmentApi::class)
class FragmentApiImpl : FragmentApi {

    override fun isAlive(fragment: BaseFragment?): Boolean =
        fragment != null && !fragment.isDetached && !fragment.isRemoving

    override fun isAlive(dialogFragment: BaseDialogFragment?): Boolean =
        dialogFragment != null && !dialogFragment.isDetached && !dialogFragment.isRemoving

    override fun showDialogFragment(fragmentManager: FragmentManager, dialogFragment: BaseDialogFragment, tag: String) {
        fragmentManager.beginTransaction()
            .add(dialogFragment, tag)
            .commitAllowingStateLoss()
        CoroutineScope(Dispatchers.Main).launch {
            fragmentManager.executePendingTransactions()
        }
    }
}