package zhupf.gadgets.blur

class BlurCompat : Blur by impl() {
    companion object {
        private fun impl(): Blur {
            return StackBlur()
        }
    }
}