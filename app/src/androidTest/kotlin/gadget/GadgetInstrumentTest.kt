package gadget

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GadgetInstrumentTest {

    @Test
    fun hi() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        println("Hi, ${appContext.packageName}!")
    }
}