package gadget.basic.ui.dsl

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import gadget.basic.annotation.DslScope

//private typealias IMAGE_VIEW = ImageView
private typealias IMAGE_VIEW = AppCompatImageView

inline fun ImageView(
    context: Context,
    params: LayoutParamsDsl<*, IMAGE_VIEW>,
    lambda: (@DslScope IMAGE_VIEW).(IMAGE_VIEW) -> Unit = {},
): IMAGE_VIEW = IMAGE_VIEW(context).apply {
    params.init(this)
    lambda(this, this)
}

inline fun ViewGroup.ImageView(
    params: LayoutParamsDsl<*, IMAGE_VIEW>,
    lambda: (@DslScope IMAGE_VIEW).(IMAGE_VIEW) -> Unit = {},
): IMAGE_VIEW = IMAGE_VIEW(context).also {
    params.init(this, it)
    lambda(it, it)
}
