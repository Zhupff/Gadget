package the.gadget.common

import android.graphics.Bitmap
import android.net.Uri
import the.gadget.GadgetApplication
import the.gadget.api.globalApi
import java.io.File
import java.io.InputStream
import java.io.OutputStream

interface FileApi {
    companion object {
        val instance: FileApi by lazy { FileApi::class.globalApi() }

        const val FILE_PROVIDER_NAME: String = "the.gadget.FileProvider"

        val STORAGE_DIR: File; get() = GadgetApplication.instance.filesDir

        val CACHE_DIR: File; get() = GadgetApplication.instance.cacheDir

        val WALLPAPER_DIR: File; get() = STORAGE_DIR.resolve("wallpaper").also { it.mkdirs() }

        val WALLPAPER_TEMP_DIR: File; get() = CACHE_DIR.resolve("wallpaper").also { it.mkdirs() }

        val AVATAR_DIR: File; get() = STORAGE_DIR.resolve("avatar").also { it.mkdirs() }
    }

    fun copy(inputStream: InputStream, outputStream: OutputStream)

    fun copy(inputStream: InputStream, output: File)

    fun getInputStreamFromUri(uri: Uri): InputStream?

    fun saveBitmap(bitmap: Bitmap, file: File): File

    fun getFile(file: File): File?

    fun getFile(path: String): File?
}

fun File?.deleteIfExists() { if (this?.exists() == true) this.delete() }