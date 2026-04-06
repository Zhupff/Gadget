package gadget.basic.window

interface Orientation {

    /**
     * 竖屏
     */
    interface Portrait

    /**
     * 横屏
     */
    interface Landscape {

        /**
         * 设备顶部朝向左侧
         */
        interface L : Landscape

        /**
         * 设备顶部朝向右侧
         */
        interface R : Landscape
    }
}