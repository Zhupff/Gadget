package gadget.basic.tool

import android.graphics.Bitmap

fun Bitmap.recycleSafely() {
    if (!isRecycled) {
        recycle()
    }
}
