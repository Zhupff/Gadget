package the.gadget.common

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import the.gadget.api.globalApi

interface ResourceApi {
    companion object : ResourceApi by ResourceApi::class.globalApi()

    fun getResources(): Resources

    fun getDisplayMetrics(): DisplayMetrics

    @ColorInt
    fun getColorInt(@ColorRes id: Int): Int

    fun getDrawable(@DrawableRes id: Int): Drawable?

    fun getDimension(@DimenRes id: Int): Float

    fun dp2px(dp: Float): Float

    fun dp2pxRound(dp: Float): Int

    fun px2dp(px: Float): Float

    fun px2dpRound(px: Float): Int

    fun sp2px(sp: Float): Float

    fun sp2pxRound(sp: Float): Int

    fun px2sp(px: Float): Float

    fun px2spRound(px: Float): Int
}


val Int.dp2px: Float get() = ResourceApi.dp2px(this.toFloat())
val Int.dp2pxRound: Int get() = ResourceApi.dp2pxRound(this.toFloat())
val Float.dp2px: Float get() = ResourceApi.dp2px(this)
val Float.dp2pxRound: Int get() = ResourceApi.dp2pxRound(this)

val Int.px2dp: Float get() = ResourceApi.px2dp(this.toFloat())
val Int.px2dpRound: Int get() = ResourceApi.px2dpRound(this.toFloat())
val Float.px2dp: Float get() = ResourceApi.px2dp(this)
val Float.px2dpRound: Int get() = ResourceApi.px2dpRound(this)

val Int.sp2px: Float get() = ResourceApi.sp2px(this.toFloat())
val Int.sp2pxRound: Int get() = ResourceApi.sp2pxRound(this.toFloat())
val Float.sp2px: Float get() = ResourceApi.sp2px(this)
val Float.sp2pxRound: Int get() = ResourceApi.sp2pxRound(this)

val Int.px2sp: Float get() = ResourceApi.px2sp(this.toFloat())
val Int.px2spRound: Int get() = ResourceApi.px2spRound(this.toFloat())
val Float.px2sp: Float get() = ResourceApi.px2sp(this)
val Float.px2spRound: Int get() = ResourceApi.px2spRound(this)