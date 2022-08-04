package the.gadget.common

import android.graphics.Bitmap
import android.net.Uri
import the.gadget.api.globalApi

interface ImageApi {
    companion object {
        val instance: ImageApi by lazy { ImageApi::class.globalApi() }
    }

    suspend fun loadWallpaperBitmap(str: String): Bitmap

    suspend fun loadWallpaperBitmap(uri: Uri): Bitmap

    suspend fun loadAvatarBitmap(str: String): Bitmap

    suspend fun loadAvatarBitmap(uri: Uri): Bitmap

    fun zoom(bitmap: Bitmap, targetSize: Int): Bitmap

    fun zoomIn(bitmap: Bitmap, minSize: Int): Bitmap

    fun zoomOut(bitmap: Bitmap, maxSize: Int): Bitmap
}