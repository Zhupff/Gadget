package zhupf.gadget.module.arch

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import zhupf.gadget.module.R
import zhupf.gadget.widget.FrameLayout
import zhupf.gadget.widget.MATCH_PARENT
import zhupf.gadget.widget.asViewId

class Page(val content: Fragment? = null) : BaseDialogFragment() {
    companion object {
        const val CONTENT_CONTAINER = "ContentContainer"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Dialog(requireContext(), R.style.Gadget_PageTheme).also { dialog ->
            dialog.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FrameLayout(requireContext(), CONTENT_CONTAINER.asViewId, MATCH_PARENT to MATCH_PARENT) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (content == null) {
            dismissAllowingStateLoss()
            return
        }
        childFragmentManager.let { fm ->
            fm.beginTransaction().replace(CONTENT_CONTAINER.asViewId, content).commitAllowingStateLoss()
            fm.executePendingTransactions()
        }
    }
}