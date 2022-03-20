import org.gradle.api.Project

fun Project.isApp(): Boolean = plugins.hasPlugin("com.android.application")
fun Project.isLib(): Boolean = plugins.hasPlugin("com.android.library")