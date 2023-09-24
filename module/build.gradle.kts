import zhupf.gadget.gradle.plugin.gadget

plugins {
    id("zhupf.gadget.module")
}

android {
    namespace = "zhupf.gadget.module"
}

gadget {
    repo {
        widget(compile = "kapt", depend = "implementation")
    }
}