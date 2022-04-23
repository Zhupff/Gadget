package the.gadget.modulebase.common

import the.gadget.modulebase.api.apiInstance
import the.gadget.modulebase.application.ApplicationApi
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
}