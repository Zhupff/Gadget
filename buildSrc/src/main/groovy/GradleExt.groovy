import groovy.xml.XmlSlurper
import org.gradle.api.Project

final class GradleExt {
    private GradleExt() {}

    static String getProjectPackage(Project project) {
        return new XmlSlurper(false, false)
            .parse(new File(project.projectDir, "src/main/AndroidManifest.xml"))
            .getProperty("@package")
            .toString()
    }
}