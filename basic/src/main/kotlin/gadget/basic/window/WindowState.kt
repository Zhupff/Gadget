package gadget.basic.window

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import gadget.basic.Gadget
import gadget.basic.tool.dp
import gadget.basic.tool.mutable
import java.util.concurrent.atomic.AtomicBoolean

sealed class WindowState(
    val width: Int,
    val height: Int,
) : Orientation {

    companion object {

        private val once = AtomicBoolean(false)

        val observable: LiveData<WindowState> = MutableLiveData()

        fun init() {
            if (once.compareAndSet(false, true)) {
                Gadget.AppLifecycle.observe(Gadget.AppLifecycle, object : Observer<Gadget.AppLifecycle.State> {
                    override fun onChanged(value: Gadget.AppLifecycle.State) {
                        if (value !is Gadget.AppLifecycle.State.OnAppConfigurationChanged) {
                            return
                        }
                        val configuration = value.newConfiguration
                        val width = configuration.screenWidthDp.dp
                        val height = configuration.screenHeightDp.dp
                        val oldState = observable.value
                        if (oldState != null && oldState.width == width && oldState.height == height) {
                            return
                        }
                        val newState = if (width.toFloat() / height.toFloat() in (3F / 4F)..(4F / 3F)) {
                            if (width > height) {
                                LandscapeTablet(width, height)
                            } else {
                                PortraitTablet(width, height)
                            }
                        } else {
                            if (width > height) {
                                LandscapePhone(width, height)
                            } else {
                                PortraitPhone(width, height)
                            }
                        }
                        observable.mutable().value = newState
                    }
                })
            }
        }
    }

    class PortraitPhone(
        width: Int,
        height: Int,
    ) : WindowState(width, height), Orientation.Portrait, Mode.Phone

    class LandscapePhone(
        width: Int, height: Int,
    ) : WindowState(width, height), Orientation.Landscape, Mode.Phone

    class PortraitTablet(
        width: Int, height: Int,
    ) : WindowState(width, height), Orientation.Portrait, Mode.Tablet

    class LandscapeTablet(
        width: Int, height: Int,
    ) : WindowState(width, height), Orientation.Landscape, Mode.Tablet
}