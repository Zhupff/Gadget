package zhupf.gadget.widget

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.MultiAutoCompleteTextView
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.AppCompatToggleButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.widget.NestedScrollView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import zhupf.gadget.widget.attr.CornerClip
import zhupf.gadget.widget.attr.ICornerClip
import zhupf.gadget.widget.attr.IInsetFit
import zhupf.gadget.widget.attr.InsetFit
import zhupf.gadget.widget.listener.ViewOnSingleClickListener

// region ID

private val VIEW_ID_CACHE: HashMap<String, Int> by lazy { HashMap() }

val View.PARENT_ID: Int; get() = (parent as? View)?.id ?: View.NO_ID

val String?.asViewId: Int; get() {
    this ?: return View.NO_ID
    if (VIEW_ID_CACHE.containsKey(this)) {
        val id = VIEW_ID_CACHE[this]
        if (id != null) return id
    }
    return View.generateViewId().also { VIEW_ID_CACHE[this] = it }
}

inline fun <reified T : View> View.findViewById(
    id: String?
): T? = findViewById(id.asViewId)

inline fun <reified T : View> Activity.findViewById(
    id: String?
): T? = findViewById(id.asViewId)

inline fun <reified T : View> Fragment.findViewById(
    id: Int
): T? = view?.findViewById(id)

inline fun <reified T : View> Fragment.findViewById(
    id: String?
): T? = view?.findViewById(id.asViewId)

inline fun <reified T : View> Dialog.findViewById(
    id: String?
): T? = findViewById(id.asViewId)

inline fun <reified T : View> RecyclerView.ViewHolder.findViewById(
    id: Int
): T? = itemView.findViewById(id)

inline fun <reified T : View> RecyclerView.ViewHolder.findViewById(
    id: String?
): T? = itemView.findViewById(id.asViewId)

// endregion
// region LayoutParams

const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT

typealias ViewLayoutParams = ViewGroup.LayoutParams
typealias MarginLayoutParams = ViewGroup.MarginLayoutParams
typealias FrameLayoutParams = FrameLayout.LayoutParams
typealias LinearLayoutParams = LinearLayout.LayoutParams
typealias RelativeLayoutParams = RelativeLayout.LayoutParams
typealias ConstraintLayoutParams = ConstraintLayout.LayoutParams
typealias DrawerLayoutParams = DrawerLayout.LayoutParams

val ConstraintLayoutParams.UNSET: Int; get() = ConstraintLayoutParams.UNSET
val ConstraintLayoutParams.PARENT_ID: Int; get() = ConstraintLayoutParams.PARENT_ID

fun ConstraintLayoutParams.unsetHorizontal() {
    startToStart = UNSET
    startToEnd = UNSET
    endToStart = UNSET
    endToEnd = UNSET
}
fun ConstraintLayoutParams.unsetVertical() {
    topToTop = UNSET
    topToBottom = UNSET
    bottomToTop = UNSET
    bottomToBottom = UNSET
}
fun ConstraintLayoutParams.unset() {
    unsetHorizontal()
    unsetVertical()
}
fun ConstraintLayoutParams.centerHorizontal() {
    startToStart = PARENT_ID
    endToEnd = PARENT_ID
}
fun ConstraintLayoutParams.centerVertical() {
    topToTop = PARENT_ID
    bottomToBottom = PARENT_ID
}
fun ConstraintLayoutParams.center() {
    centerHorizontal()
    centerVertical()
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParamsAs(
    block: (@DslScope T).() -> Unit
): T = (layoutParams as T).also {
    block(it)
    layoutParams = it
}

// endregion
// region Initialize

fun <T : View> T.initialize(
    @IdRes id: Int,
    layoutParams: ViewLayoutParams,
    parent: ViewGroup?,
    index: Int = -1,
) = apply {
    if (id != View.NO_ID) {
        this.id = id
    }
    if (parent != null) {
        parent.addView(this, index, layoutParams)
    } else {
        this.layoutParams = layoutParams
    }
}

fun <T : View> T.initialize(
    @IdRes id: Int,
    size: Pair<Int, Int>,
    parent: ViewGroup?,
    index: Int = -1,
) = initialize(id, ViewLayoutParams(size.first, size.second), parent, index)

// endregion
// region Listener

fun View.onClick(
    block: (View?) -> Unit
) {
    setOnClickListener(block)
}

fun View.onLongClick(
    block: (View?) -> Boolean
) {
    setOnLongClickListener(block)
}

fun View.onSingleClick(
    interval: Long = 500L,
    block: (View?) -> Unit
) {
    setOnClickListener(object : ViewOnSingleClickListener(interval = interval) {
        override fun onSingleClick(v: View?) {
            super.onSingleClick(v)
            block(v)
        }
    })
}

// endregion


// region CornerClip
inline fun ICornerClip.cornerClip(
    block: (@DslScope CornerClip).() -> Unit
) {
    cornerClip.apply(block).invalidate()
}
// endregion


// region InsetFit
inline fun IInsetFit.insetFit(
    block: (@DslScope InsetFit).() -> Unit
) {
    insetFit.apply(block).invalidate()
}
// endregion


// region JustForDsl

private class ForWidgetDsl {
    @WidgetDsl private lateinit var ViewX: ViewX
    @WidgetDsl private lateinit var ImageViewX: ImageViewX
    @WidgetDsl private lateinit var TextViewX: TextViewX
    @WidgetDsl private lateinit var EditTextX: EditTextX
    @WidgetDsl private lateinit var View: View
    @WidgetDsl private lateinit var ImageView: ImageView
    @WidgetDsl private lateinit var TextView: TextView
    @WidgetDsl private lateinit var EditText: EditText
    @WidgetDsl private lateinit var Button: Button
    @WidgetDsl private lateinit var CheckBox: CheckBox
    @WidgetDsl private lateinit var ImageButton: ImageButton
    @WidgetDsl private lateinit var ToggleButton: ToggleButton
    @WidgetDsl private lateinit var CheckedTextView: CheckedTextView
    @WidgetDsl private lateinit var AutoCompleteTextView: AutoCompleteTextView
    @WidgetDsl private lateinit var MultiAutoCompleteTextView: MultiAutoCompleteTextView
    @WidgetDsl private lateinit var SeekBar: SeekBar
    @WidgetDsl private lateinit var Guideline: Guideline
    @WidgetDsl private lateinit var AppCompatImageView: AppCompatImageView
    @WidgetDsl private lateinit var AppCompatTextView: AppCompatTextView
    @WidgetDsl private lateinit var AppCompatEditText: AppCompatEditText
    @WidgetDsl private lateinit var AppCompatButton: AppCompatButton
    @WidgetDsl private lateinit var AppCompatCheckBox: AppCompatCheckBox
    @WidgetDsl private lateinit var AppCompatImageButton: AppCompatImageButton
    @WidgetDsl private lateinit var AppCompatToggleButton: AppCompatToggleButton
    @WidgetDsl private lateinit var AppCompatCheckedTextView: AppCompatCheckedTextView
    @WidgetDsl private lateinit var AppCompatAutoCompleteTextView: AppCompatAutoCompleteTextView
    @WidgetDsl private lateinit var AppCompatMultiAutoCompleteTextView: AppCompatMultiAutoCompleteTextView
    @WidgetDsl private lateinit var AppCompatSeekBar: AppCompatSeekBar

    @WidgetDsl private lateinit var FrameLayout: FrameLayout
    @WidgetDsl private lateinit var LinearLayout: LinearLayout
    @WidgetDsl private lateinit var RelativeLayout: RelativeLayout
    @WidgetDsl private lateinit var ConstraintLayout: ConstraintLayout
    @WidgetDsl private lateinit var RecyclerView: RecyclerView
    @WidgetDsl private lateinit var DrawerLayout: DrawerLayout
    @WidgetDsl private lateinit var ListView: ListView
    @WidgetDsl private lateinit var ScrollView: ScrollView
    @WidgetDsl private lateinit var NestedScrollView: NestedScrollView
    @WidgetDsl private lateinit var HorizontalScrollView: HorizontalScrollView
    @WidgetDsl private lateinit var FrameLayoutX: FrameLayoutX
    @WidgetDsl private lateinit var LinearLayoutX: LinearLayoutX
    @WidgetDsl private lateinit var RelativeLayoutX: RelativeLayoutX
    @WidgetDsl private lateinit var ConstraintLayoutX: ConstraintLayoutX
    @WidgetDsl private lateinit var RecyclerViewX: RecyclerViewX
}

private class ForLayoutParamsDsl {
    @LayoutParamsDsl private lateinit var viewLayoutParams: ViewGroup.LayoutParams
    @LayoutParamsDsl private lateinit var marginLayoutParams: ViewGroup.MarginLayoutParams
    @LayoutParamsDsl private lateinit var frameLayoutParams: FrameLayout.LayoutParams
    @LayoutParamsDsl private lateinit var linearLayoutParams: LinearLayout.LayoutParams
    @LayoutParamsDsl private lateinit var relativeLayoutParams: RelativeLayout.LayoutParams
    @LayoutParamsDsl private lateinit var constraintLayoutParams: ConstraintLayout.LayoutParams
    @LayoutParamsDsl private lateinit var drawerLayoutParams: DrawerLayout.LayoutParams
}