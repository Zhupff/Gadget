import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

class Publication<S : Script> internal constructor(
    val script: S,
) {

    init {
        assert(script[Publication::class.java] == null)
        script[Publication::class.java] = this
    }

    fun publish() {
        script.project.pluginManager.apply("maven-publish")
        if (System.getenv("JITPACK").toBoolean()) {
            script.project.group = "${System.getenv("GROUP")}.${System.getenv("ARTIFACT")}"
            script.project.version = System.getenv("VERSION")
        } else {
            script.project.group = "zhupf.gadgets"
            script.project.version = "0"
        }
        script.project.afterEvaluate {
            script.project.extensions.configure(PublishingExtension::class.java) {
                repositories {
                    mavenLocal()
                }
                publications {
                    create("MavenLocalPublication", MavenPublication::class.java) {
                        from(
                            script.project.components.getByName(
                                when (script) {
                                    is ApplicationScript, is LibraryScript -> "release"
                                    is JvmScript -> "java"
                                    else -> throw IllegalStateException("$script can't publish")
                                }
                            )
                        )
                        groupId = script.project.group.toString()
                        artifactId = script.project.name
                        version = script.project.version.toString()
                    }
                }
            }
        }
    }
}



fun <S : Script> S.publication(
    closure: Publication<S>.() -> Unit = {},
) {
    Publication(this).closure()
}