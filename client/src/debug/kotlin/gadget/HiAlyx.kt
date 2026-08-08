package gadget

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import gadget.basic.network.HTTP
import java.io.FileDescriptor
import java.io.PrintWriter
import java.util.LinkedList

class HiAlyx : ContentProvider() {

    override fun onCreate(): Boolean = true

    override fun getType(uri: Uri): String? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String?>?): Int = 0

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(uri: Uri, projection: Array<out String?>?, selection: String?, selectionArgs: Array<out String?>?, sortOrder: String?): Cursor? = null

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String?>?): Int = 0

    override fun dump(fd: FileDescriptor?, writer: PrintWriter, args: Array<out String?>?) {
        val cmds = LinkedList<String>()
        args?.forEach {
            if (!it.isNullOrBlank()) {
                cmds.offer(it)
            }
        }
        while (cmds.isNotEmpty()) {
            when (val cmd = cmds.pop()) {
                "--baseurl" -> {
                    val value = cmds.pop()
                    HTTP.updateBaseUrl(value)
                }
            }
        }
    }
}