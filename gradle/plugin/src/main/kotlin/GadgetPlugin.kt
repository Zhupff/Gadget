import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import java.util.Properties

abstract class GadgetPlugin : Plugin<Project> {

    protected lateinit var project: Project
        private set

    protected val libs: VersionCatalog by lazy {
        this.project.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
    }

    val localProperties: Properties by lazy {
        Properties().also { it.load(project.rootProject.file("local.properties").inputStream()) }
    }

    override fun apply(target: Project) {
        this.project = target
        target.extensions.add(this.javaClass, "gadget", this)
    }
}