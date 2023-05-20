package zhupf.gadget.widget

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StyleableRes

private const val TLX = 0
private const val TLY = 1
private const val TRX = 2
private const val TRY = 3
private const val BRX = 4
private const val BRY = 5
private const val BLX = 6
private const val BLY = 7

interface ICornerClip {
    val cornerClip: CornerClip
}

class CornerClipAttr(
    val attrs: AttributeSet?,
    @StyleableRes val styleableId: IntArray,

    @StyleableRes val allCornerRadiusId: Int,

    @StyleableRes val tlCornerRadiusId: Int,
    @StyleableRes val tlCornerRadiusXId: Int,
    @StyleableRes val tlCornerRadiusYId: Int,

    @StyleableRes val trCornerRadiusId: Int,
    @StyleableRes val trCornerRadiusXId: Int,
    @StyleableRes val trCornerRadiusYId: Int,

    @StyleableRes val brCornerRadiusId: Int,
    @StyleableRes val brCornerRadiusXId: Int,
    @StyleableRes val brCornerRadiusYId: Int,

    @StyleableRes val blCornerRadiusId: Int,
    @StyleableRes val blCornerRadiusXId: Int,
    @StyleableRes val blCornerRadiusYId: Int,
)

class CornerClip(
    private val view: View,
    cornerClipAttr: CornerClipAttr,
) {
    private val currentRadius: FloatArray = FloatArray(8)
    private val pendingRadius: FloatArray = FloatArray(8)

    init {
        view.context.obtainStyledAttributes(cornerClipAttr.attrs, cornerClipAttr.styleableId).also { attr ->
            val allCornerRadius = attr.getDimension(cornerClipAttr.allCornerRadiusId, 0F)

            val tlCornerRadius = attr.getDimension(cornerClipAttr.tlCornerRadiusId, allCornerRadius)
            val tlCornerRadiusX = attr.getDimension(cornerClipAttr.tlCornerRadiusXId, tlCornerRadius)
            val tlCornerRadiusY = attr.getDimension(cornerClipAttr.tlCornerRadiusYId, tlCornerRadius)

            val trCornerRadius = attr.getDimension(cornerClipAttr.trCornerRadiusId, allCornerRadius)
            val trCornerRadiusX = attr.getDimension(cornerClipAttr.trCornerRadiusXId, trCornerRadius)
            val trCornerRadiusY = attr.getDimension(cornerClipAttr.trCornerRadiusYId, trCornerRadius)

            val brCornerRadius = attr.getDimension(cornerClipAttr.brCornerRadiusId, allCornerRadius)
            val brCornerRadiusX = attr.getDimension(cornerClipAttr.brCornerRadiusXId, brCornerRadius)
            val brCornerRadiusY = attr.getDimension(cornerClipAttr.brCornerRadiusYId, brCornerRadius)

            val blCornerRadius = attr.getDimension(cornerClipAttr.blCornerRadiusId, allCornerRadius)
            val blCornerRadiusX = attr.getDimension(cornerClipAttr.blCornerRadiusXId, blCornerRadius)
            val blCornerRadiusY = attr.getDimension(cornerClipAttr.blCornerRadiusYId, blCornerRadius)

            floatArrayOf(
                tlCornerRadiusX, tlCornerRadiusY,
                trCornerRadiusX, trCornerRadiusY,
                brCornerRadiusX, brCornerRadiusY,
                blCornerRadiusX, blCornerRadiusY
            ).also { radius ->
                radius.copyInto(currentRadius)
                radius.copyInto(pendingRadius)
            }
        }.recycle()
        view.addOnLayoutChangeListener { v, l1, t1, r1, b1, l2, t2, r2, b2 ->
            if (v == view && (r1 - l1 != r2 - l2 || b1 - t1 != b2 - t2)) {
                adjustCornerRadius()
                view.postInvalidate()
            }
        }
    }

    private val cornerRect: RectF = RectF()
    private val cornerPath: Path = Path()
    val needToClip: Boolean; get() = currentRadius.any { it > 0F }

    var allCornerRadius: Float
        get() {
            throw IllegalStateException("Can't get allCornerRadius because each corner may has different radius.")
        }
        set(value) {
            pendingRadius.fill(value)
        }

    var tlCornerRadius: Pair<Float, Float>
        get() = currentRadius[TLX] to currentRadius[TLY]
        set(value) {
            pendingRadius[TLX] = value.first
            pendingRadius[TLY] = value.second
        }
    var tlCornerRadiusX: Float
        get() = currentRadius[TLX]
        set(value) { pendingRadius[TLX] = value }
    var tlCornerRadiusY: Float
        get() = currentRadius[TLY]
        set(value) { pendingRadius[TLY] = value }

    var trCornerRadius: Pair<Float, Float>
        get() = currentRadius[TRX] to currentRadius[TRY]
        set(value) {
            pendingRadius[TRX] = value.first
            pendingRadius[TRY] = value.second
        }
    var trCornerRadiusX: Float
        get() = currentRadius[TRX]
        set(value) { pendingRadius[TRX] = value }
    var trCornerRadiusY: Float
        get() = currentRadius[TRY]
        set(value) { pendingRadius[TRY] = value }

    var brCornerRadius: Pair<Float, Float>
        get() = currentRadius[BRX] to currentRadius[BRY]
        set(value) {
            pendingRadius[BRX] = value.first
            pendingRadius[BRY] = value.second
        }
    var brCornerRadiusX: Float
        get() = currentRadius[BRX]
        set(value) { pendingRadius[BRX] = value }
    var brCornerRadiusY: Float
        get() = currentRadius[BRY]
        set(value) { pendingRadius[BRY] = value }

    var blCornerRadius: Pair<Float, Float>
        get() = currentRadius[BLX] to currentRadius[BLY]
        set(value) {
            pendingRadius[BLX] = value.first
            pendingRadius[BLY] = value.second
        }
    var blCornerRadiusX: Float
        get() = currentRadius[BLX]
        set(value) { pendingRadius[BLX] = value }
    var blCornerRadiusY: Float
        get() = currentRadius[BLY]
        set(value) { pendingRadius[BLY] = value }

    private fun adjustCornerRadius() {
        cornerRect.set(0F, 0F, view.width.toFloat(), view.height.toFloat())
        cornerPath.reset()
        cornerPath.addRoundRect(cornerRect, currentRadius, Path.Direction.CW)
    }

    fun invalidate(sync: Boolean = true) {
        if (!currentRadius.contentEquals(pendingRadius)) {
            pendingRadius.copyInto(currentRadius)
            adjustCornerRadius()
            if (sync) view.invalidate() else view.postInvalidate()
        }
    }

    fun clip(canvas: Canvas) {
        canvas.clipPath(cornerPath)
    }
}