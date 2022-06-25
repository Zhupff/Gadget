package the.gadget.component.setting.theme

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import the.gadget.api.ResourceApi
import the.gadget.component.setting.R
import the.gadget.theme.ThemeApi
import kotlin.math.*

class RgbColorPickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var centerR: Float = 0f
    private var strokeW: Float = 0f
    private var strokeR: Float = 0f
    private var indicatorCenterX: Float = 0f
    private var indicatorCenterY: Float = 0f
    private var indicatorStrokeW: Float = 0f
    private var indicatorStrokeR: Float = 0f
    private var indicatorDegrees: Double = 0.0

    var currentColor: Int = 0; private set

    private var isDragging: Boolean = false

    private val text: String = "确认"
    private var textBaseLine: Float = 0f

    private val ringPaint: Paint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
    }

    private val centerPaint: Paint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.FILL
    }

    private val indicatorPaint: Paint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.color = Color.WHITE
    }

    private val textPaint: Paint = Paint().also {
        it.isAntiAlias = true
        it.textAlign = Paint.Align.CENTER
        it.textSize = ResourceApi.instance.getDimension(the.gadget.module.base.R.dimen.textSizeMax)
        val rect = Rect()
        it.getTextBounds(text, 0, text.length - 1, rect)
        textBaseLine = (rect.bottom + rect.top) / -2f
    }

    private val colors: IntArray by lazy {
        val step = 12
        val degrees = 360 / step
        val hsv = floatArrayOf(0f, 1f, 1f)
        IntArray(step + 1) {
            hsv[0] = it * degrees % 360f
            Color.HSVToColor(hsv)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val color = ThemeApi.instance.getCurrentScheme().value?.originArgb
            ?: ResourceApi.instance.getColorInt(the.gadget.module.base.R.color.themeOrigin)
        val hsv = floatArrayOf(0f, 1f, 1f)
        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), hsv)
        indicatorDegrees = hsv[0].toDouble()
        currentColor = Color.HSVToColor(floatArrayOf(indicatorDegrees.toFloat(), 1f, 1f))
        textPaint.color = ThemeApi.instance.getCurrentScheme().value?.onBackground
            ?: ResourceApi.instance.getColorInt(the.gadget.module.base.R.color.themeOnBackground)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!changed) return
        centerX = width / 2f
        centerY = height / 2f
        val outerR = min(centerX, centerY)
        val innerR = outerR * 0.6f
        centerR = outerR / 2f
        strokeW = outerR - innerR
        strokeR = (outerR + innerR) / 2f
        val indicatorOuterR = strokeW / 2f
        val indicatorInnerR = indicatorOuterR * 0.6f
        indicatorStrokeW = indicatorOuterR - indicatorInnerR
        indicatorStrokeR = (indicatorOuterR + indicatorInnerR) / 2f

        ringPaint.strokeWidth = strokeW
        ringPaint.shader = SweepGradient(centerX, centerY, colors, null)
        indicatorPaint.strokeWidth = indicatorStrokeW
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val distanceX = abs(event.x - centerX)
                    val distanceY = abs(event.y - centerY)
                    val distanceR = sqrt(distanceX * distanceX + distanceY * distanceY)
                    isDragging = distanceR > centerR
                }
                if (isDragging) {
                    indicatorDegrees = atan2(event.y - centerY, event.x - centerX) * 180.0 / PI
                    if (indicatorDegrees < 0) indicatorDegrees += 360
                    currentColor = Color.HSVToColor(floatArrayOf(indicatorDegrees.toFloat(), 1f, 1f))
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!isDragging) {
                    performClick()
                }
                isDragging = false
            }
            MotionEvent.ACTION_CANCEL -> {
                isDragging = false
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return super.onDraw(canvas)
        canvas.drawCircle(centerX, centerY, strokeR, ringPaint)
        val radians = indicatorDegrees / 180.0 * PI
        indicatorCenterX = centerX + cos(radians).toFloat() * strokeR
        indicatorCenterY = centerY + sin(radians).toFloat() * strokeR
        canvas.drawCircle(indicatorCenterX, indicatorCenterY, indicatorStrokeR, indicatorPaint)
        centerPaint.color = currentColor
        canvas.drawCircle(centerX, centerY, centerR, centerPaint)
        canvas.drawText(text, centerX, centerY + textBaseLine, textPaint)
    }
}