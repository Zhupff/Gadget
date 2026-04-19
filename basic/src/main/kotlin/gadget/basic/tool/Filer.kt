package gadget.basic.tool

import gadget.basic.Gadget
import gadget.basic.exception.throws
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipException
import java.util.zip.ZipFile

object Filer {

    fun save(inputStream: InputStream, outputFile: File) {
        outputFile.parentFile?.mkdirs()
        inputStream.use { iStream ->
            FileOutputStream(outputFile).use { oStream ->
                iStream.copyTo(oStream)
            }
        }
    }

    fun unzip(zipFile: File, targetDir: File, callback: (File?) -> Unit) {
        targetDir.mkdirs()
        val tempDir = targetDir.parentFile?.resolve("${targetDir}_temp${System.currentTimeMillis()}")?.also(File::mkdirs)
        if (tempDir == null) {
            if (Gadget.debuggable) {
                ZipException("unzip(${zipFile}, ${targetDir}) exception: temp dir create failed!").throws()
            }
            callback(null)
            return
        }
        val tempDirCanonicalPath = tempDir.canonicalPath
        ZipFile(zipFile).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                val file = File(tempDir, entry.name)
                if (!file.canonicalPath.startsWith(tempDirCanonicalPath)) {
                    if (Gadget.debuggable) {
                        ZipException("unzip(${zipFile}, ${targetDir}) exception: ${file.canonicalPath} illegal!").throws()
                    }
                    tempDir.deleteRecursively()
                    callback(null)
                    return
                }
                if (entry.isDirectory) {
                    file.mkdirs()
                } else {
                    file.parentFile?.mkdirs()
                    zip.getInputStream(entry).use { input ->
                        FileOutputStream(file).use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }
        if (tempDir.exists()) {
            tempDir.renameTo(targetDir)
            tempDir.deleteRecursively()
            callback(targetDir)
        }
    }
}

fun InputStream.saveTo(outputFile: File) {
    Filer.save(this, outputFile)
}

fun File.saveFrom(inputStream: InputStream) {
    Filer.save(inputStream, this)
}

fun File.unzip(targetDir: File, callback: (File?) -> Unit) {
    Filer.unzip(this, targetDir, callback)
}
