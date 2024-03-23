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
    dependency {
        gadget()
    }
}