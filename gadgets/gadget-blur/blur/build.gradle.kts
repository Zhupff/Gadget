plugins {
    id("gadget.library")
}

script {
    configuration("zhupf.gadgets.blur") {
        configure()
    }
    publication {
        publish()
    }
}