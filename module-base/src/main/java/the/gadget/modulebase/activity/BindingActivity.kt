package the.gadget.modulebase.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import the.gadget.modulebase.interfaces.ILayoutRes

abstract class BindingActivity<V : ViewDataBinding> : BaseActivity(), ILayoutRes {

    protected val binding: V by lazy {
        DataBindingUtil.setContentView(this, getLayoutRes())
    }
}