plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("ApplicationScript") {
            id = "gadget.application"
            implementationClass = "ApplicationScript"
        }
        register("LibraryScript") {
            id = "gadget.library"
            implementationClass = "LibraryScript"
        }
        register("JvmScript") {
            id = "gadget.jvm"
            implementationClass = "JvmScript"
        }
    }
}