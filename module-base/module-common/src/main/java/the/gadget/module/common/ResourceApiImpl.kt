package the.gadget.module.common

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.google.auto.service.AutoService
import the.gadget.api.ApplicationApi
import the.gadget.api.ResourceApi

@AutoService(ResourceApi::class)
class ResourceApiImpl : ResourceApi {

    override fun getResources(): Resources = ApplicationApi.instance.getApplication().resources

    override fun getDisplayMetrics(): DisplayMetrics = getResources().displayMetrics

    override fun getColorInt(@ColorRes id: Int): Int = ResourcesCompat.getColor(getResources(), id, null)

    override fun getDrawable(@DrawableRes id: Int): Drawable? = ResourcesCompat.getDrawable(getResources(), id, null)

    override fun getDimension(@DimenRes id: Int): Float = getResources().getDimension(id)

    override fun dp2px(dp: Float): Float = getDisplayMetrics().density * dp

    override fun dp2pxRound(dp: Float): Int = (dp2px(dp) + 0.5F).toInt()

    override fun px2dp(px: Float): Float = px / getDisplayMetrics().density

    override fun px2dpRound(px: Float): Int = (px2dp(px) + 0.5F).toInt()

    override fun sp2px(sp: Float): Float = TypedValue.applyDimension(2, sp, getDisplayMetrics())

    override fun sp2pxRound(sp: Float): Int = sp2px(sp).toInt()

    override fun px2sp(px: Float): Float = px / getDisplayMetrics().scaledDensity

    override fun px2spRound(px: Float): Int = (px2sp(px) + 0.5).toInt()
}