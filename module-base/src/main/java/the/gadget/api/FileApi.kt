package the.gadget.api

import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.InputStream
import java.io.OutputStream

interface FileApi {
    companion object {
        val instance: FileApi by lazy { apiInstance(FileApi::class.java) }

        const val FILE_PROVIDER_NAME: String = "the.gadget.FileProvider"

        val STORAGE_DIR: File; get() = ApplicationApi.instance.getApplication().filesDir

        val CACHE_DIR: File; get() = ApplicationApi.instance.getApplication().cacheDir

        val WALLPAPER_DIR: File; get() = STORAGE_DIR.resolve("wallpaper").also { it.mkdirs() }

        val WALLPAPER_TEMP_DIR: File; get() = CACHE_DIR.resolve("wallpaper").also { it.mkdirs() }
    }

    fun copy(inputStream: InputStream, outputStream: OutputStream)

    fun copy(inputStream: InputStream, output: File)

    fun getInputStreamFromUri(uri: Uri): InputStream?

    fun saveBitmap(bitmap: Bitmap, file: File): File

    fun getFile(file: File): File?

    fun getFile(path: String): File?
}

fun File?.deleteIfExists() { if (this?.exists() == true) this.delete() }