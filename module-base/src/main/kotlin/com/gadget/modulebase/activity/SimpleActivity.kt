package com.gadget.modulebase.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gadget.modulebase.interfaces.ILayoutRes

abstract class SimpleActivity<V : ViewDataBinding> : BaseActivity(), ILayoutRes {

    protected val binding: V by lazy {
        DataBindingUtil.setContentView(this, getLayoutRes())
    }
}