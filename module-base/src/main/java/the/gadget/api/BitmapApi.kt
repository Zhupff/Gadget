package the.gadget.api

import android.graphics.Bitmap

interface BitmapApi {
    companion object {
        val instance: BitmapApi by lazy { apiInstance(BitmapApi::class.java) }
    }

    fun zoom(bitmap: Bitmap, targetSize: Int): Bitmap

    fun zoomIn(bitmap: Bitmap, minSize: Int): Bitmap

    fun zoomOut(bitmap: Bitmap, maxSize: Int): Bitmap
}