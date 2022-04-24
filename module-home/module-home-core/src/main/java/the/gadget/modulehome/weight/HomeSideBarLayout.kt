package the.gadget.modulehome.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import the.gadget.modulebase.weight.common.CommonConstraintLayout
import the.gadget.modulehomecore.R
import the.gadget.modulehomecore.databinding.HomeSideBarLayoutBinding

class HomeSideBarLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CommonConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: HomeSideBarLayoutBinding = DataBindingUtil
        .inflate(LayoutInflater.from(context), R.layout.home_side_bar_layout, this, true)
}