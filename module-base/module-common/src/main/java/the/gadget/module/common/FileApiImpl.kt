package the.gadget.module.common

import android.graphics.Bitmap
import android.net.Uri
import the.gadget.GadgetApplication
import the.gadget.api.GlobalApi
import the.gadget.common.FileApi
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

@GlobalApi(FileApi::class)
class FileApiImpl : FileApi {

    override fun copy(inputStream: InputStream, outputStream: OutputStream) {
        val buffer = ByteArray(2048)
        inputStream.use { iStream ->
            outputStream.use { oStream ->
                while (true) {
                    val length = iStream.read(buffer)
                    if (length != -1) {
                        oStream.write(buffer, 0, length)
                    } else {
                        oStream.flush()
                        break
                    }
                }
            }
        }
    }

    override fun copy(inputStream: InputStream, output: File) {
        copy(inputStream, FileOutputStream(output))
    }

    override fun getInputStreamFromUri(uri: Uri): InputStream? =
        GadgetApplication.instance.contentResolver.openInputStream(uri)

    override fun saveBitmap(bitmap: Bitmap, file: File): File {
        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
        return file
    }

    override fun getFile(file: File): File? = getFile(file.path)

    override fun getFile(path: String): File? {
        val file = File(path)
        if (!file.exists()) {
            val parent = file.parentFile ?: return null
            if (!parent.exists()) {
                parent.mkdirs()
            }
        }
        return file
    }
}