plugins {
    id("gadget.library")
}

script {
    configuration("zhupf.gadget.basic") {
        configure()
    }
    dependency {
        common()
        gadgets {
            basicAndroid()
            logger()
        }
    }
}