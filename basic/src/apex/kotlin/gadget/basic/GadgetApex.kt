package gadget.basic

class GadgetApex : Gadget() {

    companion object {
        val application: GadgetApex by lazy { Gadget.application as GadgetApex }
    }
}