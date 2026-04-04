package gadget.basic.theme

import android.view.View
import androidx.lifecycle.LiveData
import gadget.basic.annotation.DslScope

interface Theme {

    val id: String
    
    val primaryColor: Int
    
    val onPrimaryColor: Int
    
    val backgroundColor: Int

    val foregroundColor: Int
    
    val surfaceColor: Int
    
    val outlineColor: Int
    
    val errorColor: Int
    
    val onErrorColor: Int
}

fun View.theme(lambda: (@DslScope Theme).() -> Unit) {
    ThemeSubscriber.get(this).subscribe(action = lambda)
}

fun View.subscribeTheme(observable: LiveData<out Theme>?, lambda: (@DslScope Theme).() -> Unit = {}) {
    ThemeSubscriber.get(this).subscribe(observable, lambda)
}
