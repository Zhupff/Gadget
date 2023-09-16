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
        register("GadgetApplicationPlugin") {
            id = "zhupf.gadget.application"
            implementationClass = "zhupf.gadget.gradle.plugin.GadgetApplicationPlugin"
        }
        register("GadgetModulePlugin") {
            id = "zhupf.gadget.module"
            implementationClass = "zhupf.gadget.gradle.plugin.GadgetModulePlugin"
        }
        register("AndroidRepoPlugin") {
            id = "zhupf.gadget.android.repo"
            implementationClass = "zhupf.gadget.gradle.plugin.AndroidRepoPlugin"
        }
        register("KotlinRepoPlugin") {
            id = "zhupf.gadget.kotlin.repo"
            implementationClass = "zhupf.gadget.gradle.plugin.KotlinRepoPlugin"
        }
    }
}