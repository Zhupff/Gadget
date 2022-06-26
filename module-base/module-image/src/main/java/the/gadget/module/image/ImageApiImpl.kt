package the.gadget.module.image

import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.auto.service.AutoService
import the.gadget.api.ApplicationApi
import the.gadget.api.DeviceApi
import the.gadget.api.ImageApi
import kotlin.math.roundToInt
import kotlin.math.sqrt

@AutoService(ImageApi::class)
class ImageApiImpl : ImageApi {

    override suspend fun loadWallpaperBitmap(str: String): Bitmap {
        return Glide.with(ApplicationApi.instance.getApplication())
            .asBitmap()
            .load(str)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .centerCrop()
            .submit(DeviceApi.instance.screenWidth(), DeviceApi.instance.screenHeight())
            .get()
    }

    override suspend fun loadWallpaperBitmap(uri: Uri): Bitmap {
        return Glide.with(ApplicationApi.instance.getApplication())
            .asBitmap()
            .load(uri)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .centerCrop()
            .submit(DeviceApi.instance.screenWidth(), DeviceApi.instance.screenHeight())
            .get()
    }

    override suspend fun loadAvatarBitmap(str: String): Bitmap {
        return Glide.with(ApplicationApi.instance.getApplication())
            .asBitmap()
            .load(str)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .centerCrop()
            .submit(480, 480)
            .get()
    }

    override suspend fun loadAvatarBitmap(uri: Uri): Bitmap {
        return Glide.with(ApplicationApi.instance.getApplication())
            .asBitmap()
            .load(uri)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .centerCrop()
            .submit(480, 480)
            .get()
    }

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