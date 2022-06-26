package the.gadget.component.setting.theme

import android.view.ViewGroup
import com.google.auto.service.AutoService
import kotlinx.coroutines.*
import the.gadget.activity.toBaseActivity
import the.gadget.api.singleToastS
import the.gadget.component.home.HomeOption
import the.gadget.component.setting.R
import the.gadget.component.setting.databinding.HomeOptionThemeViewHolderBinding
import the.gadget.fragment.FragmentApi
import the.gadget.livedata.observe
import the.gadget.theme.ThemeApi
import the.gadget.weight.recyclerview.BindingRecyclerViewHolder
import the.gadget.weight.recyclerview.RecyclerViewHolder
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AutoService(HomeOption::class)
class HomeOptionTheme : HomeOption(Option.Theme) {
    companion object {
        private val THEME_OPTION_LOCK: AtomicBoolean = AtomicBoolean(false)

        fun isThemeOptionLocked(): Boolean = THEME_OPTION_LOCK.get()

        fun launch(
            context: CoroutineContext = EmptyCoroutineContext,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            block: suspend CoroutineScope.() -> Unit
        ): Job {
            THEME_OPTION_LOCK.set(true)
            return CoroutineScope(Dispatchers.IO).launch(context, start, block).also {
                it.invokeOnCompletion { THEME_OPTION_LOCK.set(false) }
            }
        }
    }

    override fun createViewHolder(container: ViewGroup): RecyclerViewHolder =
        ViewHolder(container, R.layout.home_option_theme_view_holder).also { it.onCreate() }

    override fun bindViewHolder(viewHolder: RecyclerViewHolder) { (viewHolder as ViewHolder).onBind()}

    private class ViewHolder(container: ViewGroup, id: Int) : BindingRecyclerViewHolder<HomeOptionThemeViewHolderBinding>(container, id) {

        fun onCreate() {
            ThemeApi.instance.getCurrentScheme().observe(itemView) { onBind() }
            binding.modeOptionLayout.setOnClickListener {
                if (isThemeOptionLocked()) {
                    "正在切换主题，请稍等~".singleToastS()
                } else {
                    launch { ThemeApi.instance.switchMode() }
                }
            }
            binding.themeOptionLayout.setOnClickListener {
                itemView.context.toBaseActivity()?.supportFragmentManager?.let { fm ->
                    if (isThemeOptionLocked()) {
                        "正在切换主题，请稍等~".singleToastS()
                    } else {
                        FragmentApi.instance.showDialogFragment(fm, HomeOptionThemePopupDialog())
                    }
                }
            }
        }

        fun onBind() {
            val scheme = ThemeApi.instance.getCurrentScheme().value ?: return
            if (scheme.mode.isLightMode()) {
                binding.ivModeIcon.setImageResource(R.drawable.ic_theme_light_mode)
                binding.tvModeName.text = "明亮模式"
            } else if (scheme.mode.isDarkMode()) {
                binding.ivModeIcon.setImageResource(R.drawable.ic_theme_dark_mode)
                binding.tvModeName.text = "黑暗模式"
            }
        }
    }
}