package gadget.basic.tool

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes
import gadget.basic.Gadget
import java.util.concurrent.CountDownLatch

object Toaster {

    private lateinit var disposableToast: Toast

    private val disposableLatch = CountDownLatch(1)

    private val mainHandler: Handler = Handler(Looper.getMainLooper()).also { handler ->
        handler.post {
            disposableToast = Toast.makeText(Gadget.application, "", Toast.LENGTH_SHORT)
            disposableLatch.countDown()
        }
    }

    private val singleToast: Toast
        get() {
            disposableLatch.await()
            return disposableToast
        }

    fun singleToastS(content: CharSequence) {
        toast(true, content, null, Toast.LENGTH_SHORT)
    }

    fun singleToastL(content: CharSequence) {
        toast(true, content, null, Toast.LENGTH_LONG)
    }

    fun singleToastS(@StringRes id: Int) {
        toast(true, null, id, Toast.LENGTH_SHORT)
    }

    fun singleToastL(@StringRes id: Int) {
        toast(true, null, id, Toast.LENGTH_LONG)
    }

    fun toastS(content: CharSequence) {
        toast(false, content, null, Toast.LENGTH_SHORT)
    }

    fun toastL(content: CharSequence) {
        toast(false, content, null, Toast.LENGTH_LONG)
    }

    fun toastS(@StringRes id: Int) {
        toast(false, null, id, Toast.LENGTH_SHORT)
    }

    fun toastL(@StringRes id: Int) {
        toast(false, null, id, Toast.LENGTH_LONG)
    }

    private fun toast(
        single: Boolean,
        content: CharSequence?,
        @StringRes id: Int?,
        duration: Int
    ) {
        mainHandler.post {
            if (single) {
                if (content != null) {
                    singleToast.setText(content)
                } else if (id != null) {
                    singleToast.setText(id)
                } else {
                    throw IllegalArgumentException("both content and id are null.")
                }
                singleToast.duration = duration
                singleToast.show()
            } else {
                if (content != null) {
                    Toast.makeText(Gadget.application, content, duration).show()
                } else if (id != null) {
                    Toast.makeText(Gadget.application, id, duration).show()
                } else {
                    throw IllegalArgumentException("both content and id are null.")
                }
            }
        }
    }
}

fun CharSequence.singleToastS() {
    Toaster.singleToastS(this)
}

fun CharSequence.singleToastL() {
    Toaster.singleToastL(this)
}

fun Int.singleToastS() {
    Toaster.singleToastS(this)
}

fun Int.singleToastL() {
    Toaster.singleToastL(this)
}

fun CharSequence.toastS() {
    Toaster.toastS(this)
}

fun CharSequence.toastL() {
    Toaster.toastL(this)
}

fun Int.toastS() {
    Toaster.toastS(this)
}

fun Int.toastL() {
    Toaster.toastL(this)
}