package transform

import com.android.build.api.transform.TransformInvocation
import org.gradle.api.Project
import java.io.File

class TransformContext(project: Project, transformInvocation: TransformInvocation) {

    val variantName: String = transformInvocation.context.variantName

    val isDebug: Boolean = variantName.contains("debug", ignoreCase = true)

    val isRelease: Boolean = variantName.contains("release", ignoreCase = true)

    val isIncremental: Boolean = transformInvocation.isIncremental

    val buildDir: File = project.buildDir

    val tempDir: File = transformInvocation.context.temporaryDir
}