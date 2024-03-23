plugins {
    id("gadget.jvm")
    `kotlin-dsl`
}

script {
    configuration {
        configure()
    }
    publication {
        publish()
    }
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(libs.android.gradle.plugin)
    implementation(project(":gadgets:api"))
}

gradlePlugin {
    plugins {
        register("Gadgets") {
            id = "zhupf.gadgets"
            implementationClass = "zhupf.gadgets.Gadgets"
        }
    }
}