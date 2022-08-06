package the.gadget.theme

import androidx.lifecycle.LiveData

interface ThemeContext {

    fun getCurrentScheme(): LiveData<Scheme>
}