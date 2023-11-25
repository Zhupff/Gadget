plugins {
    id("zhupf.gadget.application")
}

gadget {
    APPLICATION("zhupf.gadget")

    dependency {
        components {
            component("homepage") {
                feature()
            }
        }
        modules()
        common()
    }
}