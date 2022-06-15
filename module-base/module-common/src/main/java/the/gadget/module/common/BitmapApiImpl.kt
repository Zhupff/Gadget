package the.gadget.module.common

import android.graphics.Bitmap
import com.google.auto.service.AutoService
import the.gadget.api.BitmapApi
import kotlin.math.roundToInt
import kotlin.math.sqrt

@AutoService(BitmapApi::class)
class BitmapApiImpl : BitmapApi {

    override fun zoom(bitmap: Bitmap, targetSize: Int): Bitmap {
        val curSize = bitmap.width * bitmap.height
        if (curSize in (targetSize * 0.95F).roundToInt()..(targetSize * 1.05F).roundToInt()) return bitmap
        val scale = sqrt(targetSize.toFloat() / curSize.toFloat())
        val targetWidth = (bitmap.width * scale).roundToInt()
        val targetHeight = (bitmap.height * scale).roundToInt()
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }

    override fun zoomIn(bitmap: Bitmap, minSize: Int): Bitmap {
        if (bitmap.width * bitmap.height >= minSize) return bitmap
        var targetWidth = bitmap.width
        var targetHeight = bitmap.height
        var scale = sqrt(minSize.toFloat() / (targetWidth * targetHeight).toFloat())
        while (targetWidth * targetHeight < minSize) {
            targetWidth = (targetWidth * scale).roundToInt()
            targetHeight = (targetHeight * scale).roundToInt()
            scale = 1.05F
        }
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }

    override fun zoomOut(bitmap: Bitmap, maxSize: Int): Bitmap {
        if (bitmap.width * bitmap.height <= maxSize) return bitmap
        var targetWidth = bitmap.width
        var targetHeight = bitmap.height
        var scale = sqrt(maxSize.toFloat() / (targetWidth * targetHeight).toFloat())
        while (targetWidth * targetHeight > maxSize) {
            targetWidth = (targetWidth * scale).roundToInt()
            targetHeight = (targetHeight * scale).roundToInt()
            scale = 0.95F
        }
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }
}