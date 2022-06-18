package the.gadget.component.theme

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.google.auto.service.AutoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import the.gadget.activity.toBaseActivity
import the.gadget.component.home.HomeOption
import the.gadget.component.theme.databinding.HomeOptionThemeViewHolderBinding
import the.gadget.fragment.FragmentApi
import the.gadget.livedata.observeLifecycleOrForever
import the.gadget.theme.Palette
import the.gadget.theme.ThemeApi
import the.gadget.weight.listener.ViewOnAttachStateChangeListener
import the.gadget.weight.recyclerview.BindingRecyclerViewHolder
import the.gadget.weight.recyclerview.RecyclerViewHolder

@AutoService(HomeOption::class)
class HomeOptionTheme : HomeOption(Option.Theme) {

    override fun createViewHolder(container: ViewGroup): RecyclerViewHolder =
        ViewHolder(container, R.layout.home_option_theme_view_holder).also { it.onCreate() }

    override fun bindViewHolder(viewHolder: RecyclerViewHolder) { (viewHolder as ViewHolder).onBind()}

    private class ViewHolder(container: ViewGroup, id: Int) : BindingRecyclerViewHolder<HomeOptionThemeViewHolderBinding>(container, id) {

        fun onCreate() {
            itemView.addOnAttachStateChangeListener(object : ViewOnAttachStateChangeListener() {
                private val observer = Observer<Palette> { onBind() }
                override fun onViewAttachedToWindow(v: View?) {
                    ThemeApi.instance.getCurrentTheme().observeLifecycleOrForever(v?.findViewTreeLifecycleOwner(), observer)
                }
                override fun onViewDetachedFromWindow(v: View?) {
                    ThemeApi.instance.getCurrentTheme().removeObserver(observer)
                }
            })
            binding.modeOptionLayout.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    ThemeApi.instance.switchThemeMode()
                }
            }
            binding.themeOptionLayout.setOnClickListener {
                itemView.context.toBaseActivity()?.supportFragmentManager?.let { fm ->
                    FragmentApi.instance.showDialogFragment(fm, HomeOptionThemePopupDialog())
                }
            }
        }

        fun onBind() {
            val palette = ThemeApi.instance.getCurrentTheme().value ?: return
            if (palette.mode.isLightMode()) {
                binding.ivModeIcon.setImageResource(R.drawable.ic_theme_light_mode)
                binding.tvModeName.text = "明亮模式"
            } else if (palette.mode.isDarkMode()) {
                binding.ivModeIcon.setImageResource(R.drawable.ic_theme_dark_mode)
                binding.tvModeName.text = "黑暗模式"
            }
        }
    }
}