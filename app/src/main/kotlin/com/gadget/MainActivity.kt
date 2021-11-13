package com.gadget

import android.os.Bundle
import com.gadget.databinding.MainActivityBinding
import com.gadget.modulebase.activity.SimpleActivity

class MainActivity : SimpleActivity<MainActivityBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
    }

    override fun getLayoutRes(): Int = R.layout.main_activity
}