package the.gadget.fragment

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import the.gadget.interfaces.ILayoutRes

abstract class BindingFragment<V : ViewDataBinding> : BaseFragment(), ILayoutRes {

    protected val binding: V by lazy {
        DataBindingUtil.inflate(layoutInflater, getLayoutRes(), contentView, false)
    }
}