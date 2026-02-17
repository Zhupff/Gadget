package gadget.basic

class GadgetPlus : Gadget() {

    companion object {
        val application: GadgetPlus by lazy { Gadget.application as GadgetPlus }
    }
}