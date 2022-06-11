package the.gadget.fragment

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import the.gadget.interfaces.ILayoutRes

abstract class BindingDialogFragment<V : ViewDataBinding> : BaseDialogFragment(), ILayoutRes {

    protected val binding: V by lazy {
        DataBindingUtil.inflate(layoutInflater, getLayoutRes(), contentView, false)
    }
}