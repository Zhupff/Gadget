package gadget.basic

class GadgetLite : Gadget() {

    companion object {
        val application: GadgetLite by lazy { Gadget.application as GadgetLite }
    }
}