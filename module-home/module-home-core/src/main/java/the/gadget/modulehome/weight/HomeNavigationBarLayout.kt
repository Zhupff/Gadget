package the.gadget.modulehome.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import the.gadget.modulebase.interfaces.ILayoutRes
import the.gadget.modulebase.weight.common.CommonFrameLayout
import the.gadget.modulehomecore.R
import the.gadget.modulehomecore.databinding.HomeNavigationBarLayoutBinding

class HomeNavigationBarLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CommonFrameLayout(context, attrs, defStyleAttr), ILayoutRes {

    val binding: HomeNavigationBarLayoutBinding = DataBindingUtil
        .inflate(LayoutInflater.from(context), getLayoutRes(), this, true)

    override fun getLayoutRes(): Int = R.layout.home_navigation_bar_layout
}