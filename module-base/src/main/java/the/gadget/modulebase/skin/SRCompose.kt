package the.gadget.modulebase.skin

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import kotlin.math.roundToInt

private val api: SkinApi by lazy { SkinApi.instance }

@Composable
fun srColor(@ColorRes id: Int, skinPackage: SkinPackage = api.getSelectedStateSkinPackage()): Color {
    return Color(api.getColorInt(skinPackage, id))
}

@Composable
fun srDrawablePainter(@DrawableRes id: Int, skinPackage: SkinPackage = api.getSelectedStateSkinPackage()): Painter {
    return SRDrawablePainter(api.getDrawable(skinPackage, id)
        ?: throw IllegalArgumentException("DrawableRes $id in skin-package(${skinPackage}) is invalid."))
}


private class SRDrawablePainter(val drawable: Drawable) : Painter() {
    override val intrinsicSize: Size = Size(drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())

    override fun DrawScope.onDraw() {
        drawIntoCanvas { canvas ->
            drawable.setBounds(0, 0, size.width.roundToInt(), size.height.roundToInt())
            drawable.draw(canvas.nativeCanvas)
        }
    }
}