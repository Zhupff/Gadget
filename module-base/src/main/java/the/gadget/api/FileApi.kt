package the.gadget.api

import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.InputStream
import java.io.OutputStream

interface FileApi {
    companion object {
        val instance: FileApi by lazy { apiInstance(FileApi::class.java) }

        val CACHE_DIR: File by lazy { ApplicationApi.instance.getApplication().cacheDir }
    }

    fun copy(inputStream: InputStream, outputStream: OutputStream)

    fun copy(inputStream: InputStream, output: File)

    fun getInputStreamFromUri(uri: Uri): InputStream?

    fun saveBitmap(bitmap: Bitmap, file: File): File

    fun getFile(file: File): File?

    fun getFile(path: String): File?
}