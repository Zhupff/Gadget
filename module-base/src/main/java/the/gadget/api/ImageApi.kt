package the.gadget.api

import android.graphics.Bitmap
import android.net.Uri

interface ImageApi {
    companion object {
        val instance: ImageApi by lazy { apiInstance(ImageApi::class.java) }
    }

    suspend fun loadWallpaperBitmap(str: String): Bitmap

    suspend fun loadWallpaperBitmap(uri: Uri): Bitmap

    suspend fun loadAvatarBitmap(str: String): Bitmap

    suspend fun loadAvatarBitmap(uri: Uri): Bitmap

    fun zoom(bitmap: Bitmap, targetSize: Int): Bitmap

    fun zoomIn(bitmap: Bitmap, minSize: Int): Bitmap

    fun zoomOut(bitmap: Bitmap, maxSize: Int): Bitmap
}