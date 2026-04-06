package gadget.basic.window

sealed class WindowState(
    val width: Int,
    val height: Int,
    val previous: WindowState? = null,
) : Orientation {

    companion object {
    }

    class PortraitPhone(
        width: Int,
        height: Int,
        previous: WindowState? = null,
    ) : WindowState(width, height, previous), Orientation.Portrait, Mode.Phone

    abstract class LandscapePhone(
        width: Int,
        height: Int,
        previous: WindowState? = null,
    ) : WindowState(width, height, previous), Orientation.Landscape, Mode.Phone {
    }

    class LandscapePhoneL(
        width: Int,
        height: Int,
        previous: WindowState? = null,
    ) : LandscapePhone(width, height, previous), Orientation.Landscape.L {
    }

    class LandscapePhoneR(
        width: Int,
        height: Int,
        previous: WindowState? = null,
    ) : LandscapePhone(width, height, previous), Orientation.Landscape.R {
    }

    class PortraitPad(
        width: Int,
        height: Int,
        previous: WindowState? = null,
    ) : WindowState(width, height, previous), Orientation.Portrait, Mode.Pad {
    }

    abstract class LandscapePad(
        width: Int,
        height: Int,
        previous: WindowState? = null,
    ) : WindowState(width, height, previous), Orientation.Landscape, Mode.Pad {
    }

    class LandscapePadL(
        width: Int,
        height: Int,
        previous: WindowState? = null,
    ) : LandscapePad(width, height, previous), Orientation.Landscape.L {
    }

    class LandscapePadR(
        width: Int,
        height: Int,
        previous: WindowState? = null,
    ) : LandscapePad(width, height, previous), Orientation.Landscape.R {
    }
}