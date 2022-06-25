package the.gadget.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import the.gadget.interfaces.ILayoutRes

abstract class BindingFragment<V : ViewDataBinding> : BaseFragment(), ILayoutRes {

    protected val binding: V by lazy {
        DataBindingUtil.inflate(layoutInflater, getLayoutRes(), contentView, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root
}