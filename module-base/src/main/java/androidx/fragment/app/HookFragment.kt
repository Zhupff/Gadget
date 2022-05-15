package androidx.fragment.app

import android.view.ViewGroup

abstract class HookFragment : Fragment() {

    protected val contentView: ViewGroup?; get() = mContainer
}