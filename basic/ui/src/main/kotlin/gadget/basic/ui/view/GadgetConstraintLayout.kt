package gadget.basic.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.view.WindowInsets
import androidx.annotation.UiThread
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withSave
import androidx.core.view.WindowInsetsCompat
import gadget.basic.exception.throws
import gadget.basic.ui.R
import gadget.basic.ui.common.SideGravity
import kotlin.math.max
import kotlin.math.min

open class GadgetConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var borderSize: Float = 0F
        @UiThread
        set(value) {
            val newValue = if (value < 0F) 0F else value
            if (field != newValue) {
                field = newValue
                postInvalidate()
            }
        }

    var borderShader: Drawable? = null
        @UiThread
        set(value) {
            if (field != value) {
                field = value
                postInvalidate()
            }
        }

    protected val borderPath: Path = Path()
    protected val borderRadiusArray = FloatArray(8) { 0F }

    var cornerGravity: Int = SideGravity.N
        @UiThread
        set(value) {
            if (!SideGravity.check(value)) {
                IllegalArgumentException("Not support ${value}!").throws()
            }
            if (field != value) {
                field = value
                invalidateOutline()
            }
        }

    var cornerRadius: Float = 0F
        @UiThread
        set(value) {
            val newValue = if (value < 0F) -1F else value
            if (field != newValue) {
                field = newValue
                invalidateOutline()
            }
        }

    protected val cornerRadiusArray = FloatArray(0) { 0F }

    var fitLeftInset: Boolean = false
        protected set

    var fitTopInset: Boolean = false
        protected set

    var fitRightInset: Boolean = false
        protected set

    var fitBottomInset: Boolean = false
        protected set

    init {
        context.withStyledAttributes(attrs, R.styleable.GadgetConstraintLayout) {
            borderShader = getDrawable(R.styleable.GadgetConstraintLayout_BorderShader)
            borderSize = getDimension(R.styleable.GadgetConstraintLayout_BorderSize, borderSize)
            cornerGravity = getInt(R.styleable.GadgetConstraintLayout_ClipCornerGravity, cornerGravity)
            cornerRadius = getDimension(R.styleable.GadgetConstraintLayout_ClipCornerRadius, cornerRadius)
            getInt(R.styleable.GadgetConstraintLayout_FitWindowInsets, SideGravity.N).let {
                fitLeftInset = it and SideGravity.L != 0
                fitTopInset = it and SideGravity.T != 0
                fitRightInset = it and SideGravity.R != 0
                fitBottomInset = it and SideGravity.B != 0
            }
        }
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                val diameter = min(width, height)
                val r = if (cornerRadius < 0F) diameter / 2F else min(cornerRadius, diameter / 2F)
                cornerRadiusArray.fill(0F)
                when (cornerGravity) {
                    SideGravity.N -> {
                        outline.setRect(0, 0, width, height)
                    }
                    SideGravity.L -> {
                        cornerRadiusArray[0] = r
                        cornerRadiusArray[1] = r
                        cornerRadiusArray[6] = r
                        cornerRadiusArray[7] = r
                        outline.setRoundRect(0, 0, width + r.toInt(), height, r)
                    }
                    SideGravity.T -> {
                        cornerRadiusArray[0] = r
                        cornerRadiusArray[1] = r
                        cornerRadiusArray[2] = r
                        cornerRadiusArray[3] = r
                        outline.setRoundRect(0, 0, width, height + r.toInt(), r)
                    }
                    SideGravity.R -> {
                        cornerRadiusArray[2] = r
                        cornerRadiusArray[3] = r
                        cornerRadiusArray[4] = r
                        cornerRadiusArray[5] = r
                        outline.setRoundRect(0 - r.toInt(), 0, width, height, r)
                    }
                    SideGravity.B -> {
                        cornerRadiusArray[4] = r
                        cornerRadiusArray[5] = r
                        cornerRadiusArray[6] = r
                        cornerRadiusArray[7] = r
                        outline.setRoundRect(0, 0 - r.toInt(), width, height, r)
                    }
                    SideGravity.TL -> {
                        cornerRadiusArray[0] = r
                        cornerRadiusArray[1] = r
                        outline.setRoundRect(0, 0, width + r.toInt(), height + r.toInt(), r)
                    }
                    SideGravity.TR -> {
                        cornerRadiusArray[2] = r
                        cornerRadiusArray[3] = r
                        outline.setRoundRect(0 - r.toInt(), 0, width, height + r.toInt(), r)
                    }
                    SideGravity.BR -> {
                        cornerRadiusArray[4] = r
                        cornerRadiusArray[5] = r
                        outline.setRoundRect(0 - r.toInt(), 0 - r.toInt(), width, height, r)
                    }
                    SideGravity.BL -> {
                        cornerRadiusArray[6] = r
                        cornerRadiusArray[7] = r
                        outline.setRoundRect(0, 0 - r.toInt(), width + r.toInt(), height, r)
                    }
                    SideGravity.A -> {
                        cornerRadiusArray.fill(r)
                        outline.setRoundRect(0, 0, width, height, r)
                    }
                }
                if (borderSize <= 0F || borderShader == null) {
                    borderRadiusArray.fill(0F)
                    borderPath.reset()
                } else {
                    val s = minOf(r / 2F, borderSize)
                    for (i in borderRadiusArray.indices) {
                        borderRadiusArray[i] = max(0F, cornerRadiusArray[i] - s)
                    }
                    borderPath.reset()
                    borderPath.addRoundRect(s, s, width - s, height - s, borderRadiusArray, Path.Direction.CW)
                    val shaderWidth = borderShader!!.intrinsicWidth
                    val shaderHeight = borderShader!!.intrinsicHeight
                    if (shaderWidth <= 0 || shaderHeight <= 0) {
                        if (width > height) {
                            borderShader!!.setBounds(0, -(width - height) / 2, width, height + (width - height) / 2)
                        } else {
                            borderShader!!.setBounds(-(height - width) / 2, 0, width + (height - width) / 2, height)
                        }
                    } else {
                        if (width * shaderHeight > height * shaderWidth) {
                            val diff = if (width > shaderWidth) {
                                width * shaderHeight / shaderWidth - shaderHeight
                            } else {
                                shaderHeight - width * shaderHeight / shaderWidth
                            } / 2
                            borderShader!!.setBounds(0, -diff, width, height + diff)
                        } else {
                            val diff = if (height > shaderHeight) {
                                height * shaderWidth / shaderHeight - shaderWidth
                            } else {
                                shaderWidth - height * shaderWidth / shaderHeight
                            } / 2
                            borderShader!!.setBounds(-diff, 0, width + diff, height)
                        }
                    }
                }
            }
        }
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets? {
        return super.onApplyWindowInsets(insets).also {
            onApplyWindowInsets()
        }
    }

    protected open fun onApplyWindowInsets() {
        if (rootWindowInsets == null) {
            return
        }
        val windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets, this)
        val systemBarInsets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
        val targetPaddingLeft = if (fitLeftInset) systemBarInsets.left else 0
        val targetPaddingTop = if (fitTopInset) systemBarInsets.top else 0
        val targetPaddingRight = if (fitRightInset) systemBarInsets.right else 0
        val targetPaddingBottom = if (fitBottomInset) systemBarInsets.bottom else 0
        if (paddingLeft != targetPaddingLeft ||
            paddingTop != targetPaddingTop ||
            paddingRight != targetPaddingRight ||
            paddingBottom != targetPaddingBottom) {
            setPadding(targetPaddingLeft, targetPaddingTop, targetPaddingRight, targetPaddingBottom)
        }
    }

    open fun fitWindowInsets(l: Boolean = fitLeftInset, t: Boolean = fitTopInset, r: Boolean = fitRightInset, b: Boolean = fitBottomInset) {
        if (fitLeftInset != l || fitTopInset != t || fitRightInset != r || fitBottomInset != b) {
            onApplyWindowInsets()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onApplyWindowInsets()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (borderSize > 0F && borderShader != null) {
            canvas.withSave {
                clipOutPath(borderPath)
                borderShader!!.draw(this)
            }
        }
    }
}