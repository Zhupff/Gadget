import org.gradle.api.Project

fun Project.isApp(): Boolean = plugins.hasPlugin("com.android.application")
fun Project.isLib(): Boolean = plugins.hasPlugin("com.android.library")

val Any?.hashTag: String; get() = "${this?.javaClass?.simpleName}(${hashCode()})"

private const val C_ESC = '\u001B'
private const val CSI_RESET = "$C_ESC[37m"
private const val CSI_INFO = "$C_ESC[34m"
private const val CSI_WARN = "$C_ESC[33m"
private const val CSI_ERROR = "$C_ESC[31m"
fun Any?.logI(msg: Any) = apply {
    println("$hashTag I: $CSI_INFO$msg$CSI_RESET")
}
fun Any?.logW(msg: Any) = apply {
    println("$hashTag W: $CSI_WARN$msg$CSI_RESET")
}
fun Any?.logE(msg: Any) = apply {
    println("$hashTag E: $CSI_ERROR$msg$CSI_RESET")
}