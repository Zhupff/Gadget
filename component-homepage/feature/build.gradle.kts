plugins {
    id("zhupf.gadget.library")
}

gadget {
    LIBRARY("zhupf.gadget.component.homepage.feature")

    dependency {
        components {
            component("homepage")
        }
        modules()
        gadgets {
            common()
        }
        common()
    }
}