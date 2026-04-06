package gadget.basic.window

interface Mode {

    /**
     *  长边 : 短边 > 4 : 3
     */
    interface Phone

    /**
     * 长边 : 短边 <= 4 : 3
     */
    interface Pad
}