package gadget.component.scan.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import kotlin.math.hypot
import kotlin.math.max

internal class QRCodeChoiceOverlayView(context: Context) : View(context) {

    var onCandidateClick: ((QRCodeCandidate) -> Unit)? = null
    var onOutsideClick: (() -> Unit)? = null

    private var imageWidth: Int = 0
    private var imageHeight: Int = 0
    private var candidates: List<QRCodeCandidate> = emptyList()
    private val density: Float = resources.displayMetrics.density
    private val spotRadius: Float = 14f * density
    private val touchRadius: Float = 40f * density
    private val cornerRadius: Float = 14f * density
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 2.5f * density
    }
    private val spotShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xAA000000.toInt()
        style = Paint.Style.FILL
    }
    private val spotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFD54F.toInt()
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        textSize = 13f * density
        isFakeBoldText = true
    }

    fun setChoices(imageWidth: Int, imageHeight: Int, candidates: List<QRCodeCandidate>) {
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
        this.candidates = candidates
        invalidate()
    }

    fun clearChoices() {
        imageWidth = 0
        imageHeight = 0
        candidates = emptyList()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (imageWidth <= 0 || imageHeight <= 0 || candidates.isEmpty()) {
            return
        }
        candidates.forEachIndexed { index, candidate ->
            val rect = mapRect(candidate.bounds())
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, strokePaint)
            canvas.drawCircle(rect.centerX(), rect.centerY(), spotRadius + 5f * density, spotShadowPaint)
            canvas.drawCircle(rect.centerX(), rect.centerY(), spotRadius, spotPaint)
            canvas.drawText(
                (index + 1).toString(),
                rect.centerX(),
                rect.centerY() - (textPaint.descent() + textPaint.ascent()) / 2f,
                textPaint,
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled || imageWidth <= 0 || imageHeight <= 0 || candidates.isEmpty()) {
            return false
        }
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP -> {
                performClick()
                val candidate = findCandidate(event.x, event.y)
                if (candidate != null) {
                    onCandidateClick?.invoke(candidate)
                } else {
                    onOutsideClick?.invoke()
                }
                true
            }
            else -> true
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun findCandidate(x: Float, y: Float): QRCodeCandidate? =
        candidates.mapNotNull { candidate ->
            val rect = mapRect(candidate.bounds())
            val distance = hypot(x - rect.centerX(), y - rect.centerY())
            if (rect.contains(x, y) || distance <= touchRadius) {
                candidate to distance
            } else {
                null
            }
        }.minByOrNull { (_, distance) -> distance }?.first

    private fun mapRect(rect: RectF): RectF {
        val scale = max(width / imageWidth.toFloat(), height / imageHeight.toFloat())
        val dx = (width - imageWidth * scale) / 2f
        val dy = (height - imageHeight * scale) / 2f
        return RectF(
            rect.left * scale + dx,
            rect.top * scale + dy,
            rect.right * scale + dx,
            rect.bottom * scale + dy,
        )
    }
}