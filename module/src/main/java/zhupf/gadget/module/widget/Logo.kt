package zhupf.gadget.module.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import zhupf.gadget.module.R
import zhupf.gadget.widget.ConstraintLayout
import zhupf.gadget.widget.ConstraintLayoutParams
import zhupf.gadget.widget.ConstraintLayoutX
import zhupf.gadget.widget.Guideline
import zhupf.gadget.widget.View
import zhupf.gadget.widget.WidgetDsl
import zhupf.gadget.widget.asViewId
import zhupf.gadget.widget.center
import zhupf.gadget.widget.constraintLayoutParams
import zhupf.gadget.widget.findViewById

@WidgetDsl
class Logo @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayoutX(context, attrs) {

    init {
        ConstraintLayout(
            id = "logoStatic".asViewId,
            size = 0 to 0
        ) {
            constraintLayoutParams {
                center()
                dimensionRatio = "1"
            }
            setBackgroundResource(R.drawable.logo_static)

            val hGuideLine = Guideline(
                id = "hGuidLine".asViewId
            ) {
                constraintLayoutParams {
                    orientation = ConstraintLayoutParams.HORIZONTAL
                    guidePercent = 0.5F
                }
            }

            val vGuideLine = Guideline(
                id = "vGuideLine".asViewId,
                size = 0 to 0
            ) {
                constraintLayoutParams {
                    orientation = ConstraintLayoutParams.VERTICAL
                    guidePercent = 0.5F
                }
            }

            View(
                id = "logoDynamic".asViewId,
                size = 0 to 0
            ) {
                constraintLayoutParams {
                    topToTop = hGuideLine.id
                    startToStart = vGuideLine.id
                    matchConstraintPercentWidth = 0.42F
                    matchConstraintPercentHeight = 0.42F
                }
                setBackgroundResource(R.drawable.logo_dynamic)
            }
        }
    }

    val vStatic: ConstraintLayout = findViewById("logoStatic")!!
    val vDynamic: View = findViewById("logoDynamic")!!

    private val rotationAnimation = ObjectAnimator.ofFloat(vDynamic, "rotation", 0F, 360F).also {
        it.interpolator = LinearInterpolator()
        it.repeatCount = ValueAnimator.INFINITE
        it.duration = 3000L
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        rotationAnimation.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        rotationAnimation.cancel()
    }
}