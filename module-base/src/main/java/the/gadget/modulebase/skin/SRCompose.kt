package the.gadget.modulebase.skin

import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val api: SkinApi by lazy { SkinApi.instance }

@Composable
fun srColor(@ColorRes id: Int, skinPackage: SkinPackage = api.getSelectedSkinPackage()): Color {
    return Color(api.getColorInt(skinPackage, id))
}