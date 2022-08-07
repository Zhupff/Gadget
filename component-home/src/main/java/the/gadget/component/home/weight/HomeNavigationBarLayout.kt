package the.gadget.component.home.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import the.gadget.component.home.R
import the.gadget.component.home.databinding.HomeNavigationBarLayoutBinding
import the.gadget.interfaces.ILayoutRes
import the.gadget.weight.common.CommonFrameLayout

class HomeNavigationBarLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CommonFrameLayout(context, attrs, defStyleAttr), ILayoutRes {

    val binding: HomeNavigationBarLayoutBinding = DataBindingUtil
        .inflate(LayoutInflater.from(context), getLayoutRes(), this, true)

    override fun getLayoutRes(): Int = R.layout.home_navigation_bar_layout
}