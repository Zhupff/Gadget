package the.gadget.module.common

import com.google.auto.service.AutoService
import the.gadget.api.FileApi
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

@AutoService(FileApi::class)
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
}