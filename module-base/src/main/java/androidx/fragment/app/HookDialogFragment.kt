package androidx.fragment.app

import android.view.ViewGroup

abstract class HookDialogFragment : DialogFragment() {

    protected val contentView: ViewGroup?; get() = mContainer
}