plugins {
    id("gadget.jvm")
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
    compileOnly(project(":gadgets:api"))
    implementation(libs.squareup.kotlinpoet)
}