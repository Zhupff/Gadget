package the.gadget.modulebase.resource

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import the.gadget.modulebase.api.apiInstance

interface ResourceApi {
    companion object {
        @JvmStatic
        val instance: ResourceApi by lazy { apiInstance(ResourceApi::class.java) }
    }

    fun getResources(): Resources

    fun getDisplayMetrics(): DisplayMetrics

    @ColorInt
    fun getColorInt(@ColorRes id: Int): Int

    fun getDrawable(@DrawableRes id: Int): Drawable

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


val Int.dp2px: Float get() = ResourceApi.instance.dp2px(this.toFloat())
val Int.dp2pxRound: Int get() = ResourceApi.instance.dp2pxRound(this.toFloat())
val Float.dp2px: Float get() = ResourceApi.instance.dp2px(this)
val Float.dp2pxRound: Int get() = ResourceApi.instance.dp2pxRound(this)

val Int.px2dp: Float get() = ResourceApi.instance.px2dp(this.toFloat())
val Int.px2dpRound: Int get() = ResourceApi.instance.px2dpRound(this.toFloat())
val Float.px2dp: Float get() = ResourceApi.instance.px2dp(this)
val Float.px2dpRound: Int get() = ResourceApi.instance.px2dpRound(this)

val Int.sp2px: Float get() = ResourceApi.instance.sp2px(this.toFloat())
val Int.sp2pxRound: Int get() = ResourceApi.instance.sp2pxRound(this.toFloat())
val Float.sp2px: Float get() = ResourceApi.instance.sp2px(this)
val Float.sp2pxRound: Int get() = ResourceApi.instance.sp2pxRound(this)

val Int.px2sp: Float get() = ResourceApi.instance.px2sp(this.toFloat())
val Int.px2spRound: Int get() = ResourceApi.instance.px2spRound(this.toFloat())
val Float.px2sp: Float get() = ResourceApi.instance.px2sp(this)
val Float.px2spRound: Int get() = ResourceApi.instance.px2spRound(this)