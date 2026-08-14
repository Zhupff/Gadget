package gadget.component.scan.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.camera.view.PreviewView
import gadget.basic.ui.dsl.ImageView
import gadget.basic.ui.dsl.PreviewView
import gadget.basic.ui.dsl.frameLayoutParams
import gadget.basic.ui.dsl.scope

internal class QRCodeScanView(context: Context) : FrameLayout(context) {

    val previewView: PreviewView = PreviewView({
        frameLayoutParams(MATCH_PARENT, MATCH_PARENT)
    }) {
        implementationMode = PreviewView.ImplementationMode.PERFORMANCE
        scaleType = PreviewView.ScaleType.FILL_CENTER
    }

    private val frozenImageView: ImageView = ImageView({
        frameLayoutParams(MATCH_PARENT, MATCH_PARENT)
    }) {
        scaleType = ImageView.ScaleType.CENTER_CROP
        visibility = View.GONE
        setBackgroundColor(Color.BLACK)
    }

    private val choiceOverlayView = scope(
        QRCodeChoiceOverlayView(context),
        { frameLayoutParams(MATCH_PARENT, MATCH_PARENT) },
    ) {
        visibility = View.GONE
    }

    var onCandidateClick: ((QRCodeCandidate) -> Unit)?
        get() = choiceOverlayView.onCandidateClick
        set(value) {
            choiceOverlayView.onCandidateClick = value
        }

    var onOutsideClick: (() -> Unit)?
        get() = choiceOverlayView.onOutsideClick
        set(value) {
            choiceOverlayView.onOutsideClick = value
        }

    fun showChoices(bitmap: Bitmap, candidates: List<QRCodeCandidate>) {
        frozenImageView.setImageBitmap(bitmap)
        frozenImageView.visibility = View.VISIBLE
        choiceOverlayView.setChoices(bitmap.width, bitmap.height, candidates)
        choiceOverlayView.isEnabled = true
        choiceOverlayView.visibility = View.VISIBLE
    }

    fun enableChoices(enabled: Boolean) {
        choiceOverlayView.isEnabled = enabled
    }

    fun hideChoices() {
        frozenImageView.setImageDrawable(null)
        frozenImageView.visibility = View.GONE
        choiceOverlayView.clearChoices()
        choiceOverlayView.isEnabled = true
        choiceOverlayView.visibility = View.GONE
    }

    fun release() {
        hideChoices()
        onCandidateClick = null
        onOutsideClick = null
    }
}