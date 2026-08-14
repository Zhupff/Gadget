package gadget.component.scan.ui

import android.graphics.PointF
import android.graphics.RectF
import kotlin.math.max
import kotlin.math.min

internal data class QRCodeCandidate(
    val text: String,
    val points: List<PointF>,
    val imageWidth: Int,
    val imageHeight: Int,
) {
    fun bounds(): RectF {
        if (points.isEmpty()) {
            return RectF(0f, 0f, imageWidth.toFloat(), imageHeight.toFloat())
        }
        var minX = imageWidth.toFloat()
        var minY = imageHeight.toFloat()
        var maxX = 0f
        var maxY = 0f
        points.forEach { point ->
            minX = min(minX, point.x)
            minY = min(minY, point.y)
            maxX = max(maxX, point.x)
            maxY = max(maxY, point.y)
        }

        val width = max(maxX - minX, 1f)
        val height = max(maxY - minY, 1f)
        val padding = max(max(width, height) * 0.35f, 24f)
        return RectF(
            (minX - padding).coerceAtLeast(0f),
            (minY - padding).coerceAtLeast(0f),
            (maxX + padding).coerceAtMost(imageWidth.toFloat()),
            (maxY + padding).coerceAtMost(imageHeight.toFloat()),
        )
    }
}