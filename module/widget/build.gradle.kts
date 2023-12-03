plugins {
    id("zhupf.gadget.library")
}

gadget {
    LIBRARY("zhupf.gadget.module.widget")

    dependency {
        gadgets {
            widget()
        }
        common()
    }
}