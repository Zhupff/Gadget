package gadget.component.scan

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.Rect
import android.os.SystemClock
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.ResultPoint
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import gadget.basic.tool.recycleSafely
import gadget.component.scan.ui.QRCodeCandidate

internal sealed interface QRCodeAnalyzeResult {
    data class Single(val text: String) : QRCodeAnalyzeResult

    data class Multiple(
        val bitmap: Bitmap,
        val candidates: List<QRCodeCandidate>,
    ) : QRCodeAnalyzeResult

    fun release() {
        if (this is Multiple) {
            bitmap.recycleSafely()
        }
    }
}

internal class QRCodeAnalyzer(
    private val shouldAnalyze: () -> Boolean,
    private val onAnalyzeResult: (QRCodeAnalyzeResult) -> Unit,
) : ImageAnalysis.Analyzer {

    private companion object {
        private const val SINGLE_CONFIRMATION_COUNT = 3
        private const val SINGLE_CONFIRMATION_DELAY_MS = 400L
        private const val SINGLE_TRACKING_GAP_MS = 800L
    }

    private val hints: Map<DecodeHintType, Any> = mapOf(
        DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE),
        DecodeHintType.TRY_HARDER to true,
    )
    private val reader = QRCodeMultiReader()
    private var pendingSingleText: String? = null
    private var pendingSingleCount = 0
    private var pendingSingleFirstAt = 0L
    private var pendingSingleLastAt = 0L

    override fun analyze(image: ImageProxy) {
        try {
            if (shouldAnalyze()) {
                runCatching { decode(image) }.getOrNull()?.let(onAnalyzeResult)
            }
        } finally {
            image.close()
        }
    }

    private fun decode(image: ImageProxy): QRCodeAnalyzeResult? {
        val frame = YFrame.from(image) ?: return null
        val bitmap = BinaryBitmap(
            HybridBinarizer(
                PlanarYUVLuminanceSource(
                    frame.data,
                    frame.width,
                    frame.height,
                    0,
                    0,
                    frame.width,
                    frame.height,
                    false,
                ),
            ),
        )
        val results = runCatching {
            reader.decodeMultiple(bitmap, hints).toList()
        }.also {
            reader.reset()
        }.getOrDefault(emptyList())

        if (results.isEmpty()) {
            expirePendingSingle()
            return null
        }
        if (results.size == 1) {
            return confirmSingle(results.first().text)
        }

        resetPendingSingle()
        val rotationDegrees = image.imageInfo.rotationDegrees
        val frozenBitmap = createFrozenBitmap(image, frame.cropRect, rotationDegrees)
        val candidates = results.mapIndexed { index, result ->
            val points = result.resultPoints
                ?.map { rotatePoint(it, frame.width, frame.height, rotationDegrees) }
                .orEmpty()
            QRCodeCandidate(
                text = result.text,
                points = points.ifEmpty {
                    listOf(
                        PointF(
                            frozenBitmap.width * (index + 1f) / (results.size + 1f),
                            frozenBitmap.height / 2f,
                        ),
                    )
                },
                imageWidth = frozenBitmap.width,
                imageHeight = frozenBitmap.height,
            )
        }
        return QRCodeAnalyzeResult.Multiple(frozenBitmap, candidates)
    }

    private fun createFrozenBitmap(image: ImageProxy, cropRect: Rect, rotationDegrees: Int): Bitmap {
        val source = image.toBitmap()
        val cropped = if (
            cropRect.left == 0 &&
            cropRect.top == 0 &&
            cropRect.width() == source.width &&
            cropRect.height() == source.height
        ) {
            source
        } else {
            Bitmap.createBitmap(
                source,
                cropRect.left,
                cropRect.top,
                cropRect.width(),
                cropRect.height(),
            ).also {
                source.recycleSafely()
            }
        }
        if (rotationDegrees == 0) {
            return cropped
        }
        return Bitmap.createBitmap(
            cropped,
            0,
            0,
            cropped.width,
            cropped.height,
            Matrix().apply { postRotate(rotationDegrees.toFloat()) },
            true,
        ).also { rotated ->
            if (rotated !== cropped) {
                cropped.recycleSafely()
            }
        }
    }

    private fun confirmSingle(text: String): QRCodeAnalyzeResult.Single? {
        val now = SystemClock.elapsedRealtime()
        if (text == pendingSingleText && now - pendingSingleLastAt <= SINGLE_TRACKING_GAP_MS) {
            pendingSingleCount++
            pendingSingleLastAt = now
        } else {
            pendingSingleText = text
            pendingSingleCount = 1
            pendingSingleFirstAt = now
            pendingSingleLastAt = now
        }
        if (
            pendingSingleCount < SINGLE_CONFIRMATION_COUNT ||
            now - pendingSingleFirstAt < SINGLE_CONFIRMATION_DELAY_MS
        ) {
            return null
        }
        resetPendingSingle()
        return QRCodeAnalyzeResult.Single(text)
    }

    private fun expirePendingSingle() {
        if (SystemClock.elapsedRealtime() - pendingSingleLastAt > SINGLE_TRACKING_GAP_MS) {
            resetPendingSingle()
        }
    }

    private fun resetPendingSingle() {
        pendingSingleText = null
        pendingSingleCount = 0
        pendingSingleFirstAt = 0L
        pendingSingleLastAt = 0L
    }

    private fun rotatePoint(point: ResultPoint, width: Int, height: Int, degrees: Int): PointF =
        when ((degrees % 360 + 360) % 360) {
            90 -> PointF(height - 1f - point.y, point.x)
            180 -> PointF(width - 1f - point.x, height - 1f - point.y)
            270 -> PointF(point.y, width - 1f - point.x)
            else -> PointF(point.x, point.y)
        }

    private data class YFrame(
        val data: ByteArray,
        val width: Int,
        val height: Int,
        val cropRect: Rect,
    ) {
        companion object {
            fun from(image: ImageProxy): YFrame? {
                val yPlane = image.planes.firstOrNull() ?: return null
                val cropRect = Rect(image.cropRect).apply {
                    intersect(0, 0, image.width, image.height)
                }
                if (cropRect.isEmpty) {
                    return null
                }
                val reader = PlaneReader(yPlane)
                val width = cropRect.width()
                val height = cropRect.height()
                val data = ByteArray(width * height)
                for (row in 0 until height) {
                    for (column in 0 until width) {
                        data[row * width + column] = reader.get(
                            row = cropRect.top + row,
                            column = cropRect.left + column,
                        ).toByte()
                    }
                }
                return YFrame(data, width, height, cropRect)
            }
        }
    }

    private class PlaneReader(plane: ImageProxy.PlaneProxy) {
        private val buffer = plane.buffer.duplicate()
        private val firstByte = buffer.position()
        private val limit = buffer.limit()
        private val rowStride = plane.rowStride
        private val pixelStride = plane.pixelStride

        fun get(row: Int, column: Int): Int {
            val index = firstByte + row * rowStride + column * pixelStride
            return if (index in firstByte until limit) {
                buffer.get(index).toInt() and 0xFF
            } else {
                0
            }
        }
    }
}
