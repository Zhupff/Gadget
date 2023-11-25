import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.text.SimpleDateFormat

@DslMarker
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.SOURCE)
annotation class GradleScope

internal const val ANDROID_APPLICATION_ID = "com.android.application"
internal const val ANDROID_LIBRARY_ID = "com.android.library"
internal const val KOTLIN_ANDROID_ID = "org.jetbrains.kotlin.android"
internal const val KOTLIN_JVM_ID = "org.jetbrains.kotlin.jvm"
internal const val GROOVY_ID = "groovy"
internal const val MAVEN_PUBLISH_ID = "maven-publish"

internal typealias AndroidExtension = CommonExtension<*, *, *, *, *>

internal val Project.androidExtension: AndroidExtension
    get() = if (pluginManager.hasPlugin("com.android.application"))
        applicationExtension
    else if (pluginManager.hasPlugin("com.android.library"))
        libraryExtension
    else
        throw RuntimeException()

internal val Project.applicationExtension: ApplicationExtension
    get() = extensions.getByType(ApplicationExtension::class.java)

internal val Project.libraryExtension: LibraryExtension
    get() = extensions.getByType(LibraryExtension::class.java)

internal val Project.appExtension: com.android.build.gradle.AppExtension
    get() = extensions.getByType(com.android.build.gradle.AppExtension::class.java)

internal val Project.libExtension: com.android.build.gradle.LibraryExtension
    get() = extensions.getByType(com.android.build.gradle.LibraryExtension::class.java)

internal val Project.versionCatalogExtension: VersionCatalogsExtension
    get() = extensions.getByType(VersionCatalogsExtension::class.java)

internal val Project.libs: VersionCatalog
    get() = versionCatalogExtension.named("libs")


internal val com.android.build.gradle.api.BaseVariant.variantName: String; get() = name

internal val com.android.build.gradle.api.BaseVariant.variantNameUpper: String; get() = variantName.replaceFirstChar { it.uppercaseChar() }


internal fun AndroidExtension.kotlinOptions(block: @GradleScope KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}


internal val GADGET_VERSION = SimpleDateFormat("YY.MM.dd").format(System.currentTimeMillis())