package gadget.basic.theme

import android.view.View
import androidx.lifecycle.LiveData
import gadget.basic.annotation.DslScope

interface ThemeScheme {
    
    val primaryColor: Int
    
    val onPrimaryColor: Int
    
    val backgroundColor: Int

    val foregroundColor: Int
    
    val surfaceColor: Int
    
    val outlineColor: Int
    
    val errorColor: Int
    
    val onErrorColor: Int
}

fun View.theme(
    lambda: ((ThemeScheme).() -> Unit)?,
) {
    ThemeSubscriber.get(this).subscribe(action = lambda ?: ThemeSubscriber.NO_ACTION)
}

fun View.subscribeTheme(
    observable: LiveData<out ThemeScheme>?,
    lambda: ((ThemeScheme).() -> Unit)? = null,
) {
    ThemeSubscriber.get(this).subscribe(observable, lambda ?: ThemeSubscriber.NO_ACTION)
}
