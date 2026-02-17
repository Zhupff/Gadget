package gadget.basic.theme

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**
 * 主题源，作用是分发主题方案。
 */
interface ThemeObservable : LifecycleOwner {
    fun subscribe(): LiveData<out Theme>
}