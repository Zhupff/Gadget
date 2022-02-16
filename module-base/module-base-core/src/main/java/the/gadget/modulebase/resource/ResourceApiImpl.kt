package the.gadget.modulebase.resource

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.TypedValue
import com.google.auto.service.AutoService
import the.gadget.modulebase.application.ApplicationApi

@AutoService(ResourceApi::class)
class ResourceApiImpl : ResourceApi {

    override fun getResources(): Resources = ApplicationApi.instance.getApplication().resources

    override fun getDisplayMetrics(): DisplayMetrics = getResources().displayMetrics

    override fun getColorInt(res: Int): Int = getResources().getColor(res)

    override fun getDrawable(res: Int): Drawable = getResources().getDrawable(res)

    override fun getDimension(res: Int): Float = getResources().getDimension(res)

    override fun dp2px(dp: Float): Float = getDisplayMetrics().density * dp

    override fun dp2pxRound(dp: Float): Int = (dp2px(dp) + 0.5F).toInt()

    override fun px2dp(px: Float): Float = px / getDisplayMetrics().density

    override fun px2dpRound(px: Float): Int = (px2dp(px) + 0.5F).toInt()

    override fun sp2px(sp: Float): Float = TypedValue.applyDimension(2, sp, getDisplayMetrics())

    override fun sp2pxRound(sp: Float): Int = sp2px(sp).toInt()

    override fun px2sp(px: Float): Float = px / getDisplayMetrics().scaledDensity

    override fun px2spRound(px: Float): Int = (px2sp(px) + 0.5).toInt()
}