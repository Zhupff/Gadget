package transform

import com.android.build.api.transform.TransformInvocation
import java.io.File

class TransformContext(transformInvocation: TransformInvocation) {

    val variantName: String = transformInvocation.context.variantName

    val isDebug: Boolean = variantName.contains("debug", ignoreCase = true)

    val isRelease: Boolean = variantName.contains("release", ignoreCase = true)

    val isIncremental: Boolean = transformInvocation.isIncremental

    val tempDir: File = transformInvocation.context.temporaryDir
}