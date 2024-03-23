plugins {
    id("gadget.application")
}

script {
    configuration("zhupf.gadget") {
        configure()
    }
    dependency {
        common()
        basic {
        }
        gadgets {
            basicAndroid()
        }
    }
}