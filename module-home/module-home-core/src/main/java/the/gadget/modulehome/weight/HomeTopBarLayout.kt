package the.gadget.modulehome.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import the.gadget.modulebase.interfaces.ILayoutRes
import the.gadget.modulehome.HomeApp
import the.gadget.modulehome.moduleHomeApi
import the.gadget.modulehomecore.R
import the.gadget.modulehomecore.databinding.HomeTopBarLayoutBinding

class HomeTopBarLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ILayoutRes {

    val binding: HomeTopBarLayoutBinding = DataBindingUtil
        .inflate(LayoutInflater.from(context), getLayoutRes(), this, true)

    private val allHomeAppsObserver: Observer<List<HomeApp>> = Observer { allHomeApps ->
        binding.app = allHomeApps?.find { it.selected }
    }

    override fun getLayoutRes(): Int = R.layout.home_top_bar_layout

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        moduleHomeApi.getAllHomeApps().observeForever(allHomeAppsObserver)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        moduleHomeApi.getAllHomeApps().removeObserver(allHomeAppsObserver)
    }
}