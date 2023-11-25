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
        gadgets {
            theme(merge = true)
        }
        module()
        common()
    }
}