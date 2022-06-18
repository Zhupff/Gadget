package the.gadget.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.HookDialogFragment
import the.gadget.module.base.R

abstract class BaseDialogFragment : HookDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Dialog(requireContext(), R.style.BaseDialogFragmentTheme)
}