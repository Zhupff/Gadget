plugins {
    id("zhupf.gadget.library")
}

gadget {
    LIBRARY("zhupf.gadget.module.theme")

    dependency {
        gadgets {
            theme()
        }
        common()
    }
}