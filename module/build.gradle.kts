plugins {
    id("zhupf.gadget.library")
}

gadget {
    LIBRARY("zhupf.gadget.module")

    dependency {
        gadgets {
            logger()
        }
        common()
    }
}