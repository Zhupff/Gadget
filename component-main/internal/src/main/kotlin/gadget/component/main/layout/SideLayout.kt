package gadget.component.main.layout

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
import com.google.android.material.appbar.CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
import gadget.basic.annotation.DslScope
import gadget.basic.theme.theme
import gadget.basic.tool.dp
import gadget.basic.ui.common.GravityX
import gadget.basic.ui.dsl.AppBarLayout
import gadget.basic.ui.dsl.CollapsingToolbarLayout
import gadget.basic.ui.dsl.ConstraintLayout
import gadget.basic.ui.dsl.CoordinatorLayout
import gadget.basic.ui.dsl.ImageView
import gadget.basic.ui.dsl.OnAttachStateChanged
import gadget.basic.ui.dsl.RecyclerView
import gadget.basic.ui.dsl.SimpleRecyclerViewAdapter
import gadget.basic.ui.dsl.SimpleRecyclerViewHolder
import gadget.basic.ui.dsl.TextView
import gadget.basic.ui.dsl.Toolbar
import gadget.basic.ui.dsl.View
import gadget.basic.ui.dsl.ViewId
import gadget.basic.ui.dsl.appBarLayoutParams
import gadget.basic.ui.dsl.bottomToBottomOfParent
import gadget.basic.ui.dsl.collapsingToolbarLayoutParams
import gadget.basic.ui.dsl.constraintLayoutParams
import gadget.basic.ui.dsl.coordinatorLayoutParams
import gadget.basic.ui.dsl.frameLayoutParams
import gadget.basic.ui.dsl.leftToLeftOfParent
import gadget.basic.ui.dsl.leftToRightOf
import gadget.basic.ui.dsl.marginLayoutParams
import gadget.basic.ui.dsl.onLayout
import gadget.basic.ui.dsl.rightToRightOfParent
import gadget.basic.ui.dsl.scope
import gadget.basic.ui.dsl.toolbarLayoutParams
import gadget.basic.ui.dsl.topToBottomOf
import gadget.basic.ui.dsl.topToTopOfParent
import gadget.basic.ui.listener.onSingleClick
import gadget.basic.ui.view.ConstraintLayoutX
import gadget.basic.ui.view.GradientTransparentL2R
import gadget.basic.ui.view.GravityImageView
import gadget.component.main.ComponentMainActivity
import gadget.component.main.navigation.MainNavOption
import gadget.component.main.navigation.MainNavOptionVM
import kotlin.math.absoluteValue

@DslScope
internal class SideLayout(
    private val activity: ComponentMainActivity,
) : FrameLayout(activity) {

    private lateinit var backgroundMask: View

    lateinit var appBarLayout: AppBarLayout
        private set

    private lateinit var photoLayout: View

    private lateinit var userLayout: View

    private val scope = scope {

        this@SideLayout.backgroundMask = View({ frameLayoutParams(MATCH_PARENT, MATCH_PARENT) {
            theme {
                setBackgroundColor(backgroundColor)
            }
        }})

        CoordinatorLayout {

            this@SideLayout.appBarLayout = AppBarLayout({ coordinatorLayoutParams(MATCH_PARENT, WRAP_CONTENT) {
                elevation = 0F
                isLiftOnScroll = false
                stateListAnimator = null
                setBackgroundColor(Color.TRANSPARENT)
                addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                    val percent = verticalOffset.absoluteValue.toFloat() / appBarLayout.totalScrollRange.toFloat()
                    this@SideLayout.photoLayout.alpha = 1F - percent
                    this@SideLayout.userLayout.alpha = percent
                    this@SideLayout.backgroundMask.alpha = 1F - percent
                }
                onLayout { _, _, _, _, _, _, _, _ -> setExpanded(true) }
            }}) {

                CollapsingToolbarLayout({ appBarLayoutParams(MATCH_PARENT, WRAP_CONTENT) {
                    scrollFlags = SCROLL_FLAG_SCROLL or SCROLL_FLAG_SNAP or SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                }}) {

                    Toolbar({ collapsingToolbarLayoutParams(MATCH_PARENT, WRAP_CONTENT) {
                        setContentInsetsAbsolute(0, 0)
                        collapseMode = COLLAPSE_MODE_PIN
                    }}) {

                        this@SideLayout.userLayout = ConstraintLayoutX({ toolbarLayoutParams(MATCH_PARENT, WRAP_CONTENT) {
                            fitWindowInsetGravity = GravityX.T
                        }}) {
                            val (_userName, _userId) = ViewId

                            TextView({ constraintLayoutParams(height = WRAP_CONTENT) {
                                id = _userName
                                leftToLeftOfParent()
                                rightToRightOfParent()
                                topToTopOfParent()
                                setMargins(16.dp, 16.dp, 16.dp, bottomMargin)
                                text = "User Name"
                                textSize = 24F
                                theme {
                                    setTextColor(primaryColor)
                                }
                            }})

                            TextView({ constraintLayoutParams(height = WRAP_CONTENT) {
                                id = _userId
                                leftToLeftOfParent()
                                rightToRightOfParent()
                                topToBottomOf(_userName)
                                bottomToBottomOfParent()
                                setMargins(16.dp, 10.dp, 16.dp, 16.dp)
                                text = "User ID"
                                textSize = 16F
                                theme {
                                    setTextColor(outlineColor)
                                }
                            }})
                        }
                    }

                    this@SideLayout.photoLayout = ConstraintLayout({ collapsingToolbarLayoutParams(MATCH_PARENT, WRAP_CONTENT) }) {
                        GravityImageView({ constraintLayoutParams {
                            leftToLeftOfParent()
                            rightToRightOfParent()
                            topToTopOfParent()
                            dimensionRatio = "4:3"
//                            gravity = GravityX.T
                            setImageResource(gadget.basic.R.drawable.ic_splash_logo)
                            theme {
                                imageTintList = ColorStateList.valueOf(foregroundColor)
                            }
                        }})
                    }
                }
            }

            RecyclerView({ coordinatorLayoutParams {
                behavior = AppBarLayout.ScrollingViewBehavior(context, null)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = MainNavOptionAdapter(this@SideLayout.activity)
                itemAnimator = null
            }}) {
            }
        }
    }

    private class MainNavOptionAdapter(
        private val activity: ComponentMainActivity,
    ) : SimpleRecyclerViewAdapter<FrameLayout>() {
        private val mainNavOptionVM = ViewModelProvider(activity)[MainNavOptionVM::class.java]
        private val allOptions: List<MainNavOption> = mainNavOptionVM.allProviders.map {
            it.provide(activity, mainNavOptionVM.current)
        }.sortedBy { it.id }
        private var snapshots: List<Pair<MainNavOption.OptionID, Boolean>> = emptyList()

        init {
            mainNavOptionVM.current.observe(activity) { selected ->
                val newSnapshots: List<Pair<MainNavOption.OptionID, Boolean>> = allOptions.map {
                    it.id to (it.id == selected)
                }
                val differ = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                    override fun getOldListSize(): Int = snapshots.size
                    override fun getNewListSize(): Int = newSnapshots.size
                    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean =
                        snapshots[oldPosition].first == newSnapshots[newPosition].first
                    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean =
                        snapshots[oldPosition].second == newSnapshots[newPosition].second
                })
                snapshots = newSnapshots
                differ.dispatchUpdatesTo(this)
            }
        }

        override fun getItemCount(): Int = snapshots.size

        override fun getItemViewType(position: Int): Int = snapshots[position].first.ordinal

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleRecyclerViewHolder<FrameLayout> {
            val optionId = snapshots.find { it.first.ordinal == viewType }!!.first
            val option = allOptions.find { it.id == optionId }!!
            val container = FrameLayout(parent.context).apply {
                layoutParams = marginLayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
            val itemView = if (option is MainNavOption.Simple) {
                SimpleItemView(activity, option)
            } else {
                option.view
            }
            container.addView(itemView)
            return SimpleRecyclerViewHolder(container)
        }

        override fun onBindViewHolder(holder: SimpleRecyclerViewHolder<FrameLayout>, position: Int) {
            val selected = snapshots[position].second
            val option = allOptions[position]
            holder.view.onSingleClick {
                if (!selected && option.onClick()) {
                    mainNavOptionVM.select(option)
                }
                500L
            }
        }

        private class SimpleItemView(
            private val activity: ComponentMainActivity,
            private val option: MainNavOption.Simple,
        ) : ConstraintLayout(activity), Observer<MainNavOption.OptionID> {
            private val mainNavOptionVM = ViewModelProvider(activity)[MainNavOptionVM::class.java]
            private lateinit var mask: GradientTransparentL2R
            private lateinit var icon: ImageView
            private val scope = scope({ marginLayoutParams(MATCH_PARENT, 50.dp) {
                OnAttachStateChanged({
                    mainNavOptionVM.current.observeForever(this@SimpleItemView)
                }, {
                    mainNavOptionVM.current.removeObserver(this@SimpleItemView)
                })
            }}) {
                mask = GradientTransparentL2R({ constraintLayoutParams {
                    leftToLeftOfParent()
                    rightToRightOfParent()
                    topToTopOfParent()
                    bottomToBottomOfParent()
                    alpha = 0.618F
                    theme {
                        setBackgroundColor(outlineColor)
                    }
                }})
                icon = ImageView({ constraintLayoutParams(30.dp, 30.dp) {
                    id = generateViewId()
                    leftToLeftOfParent()
                    topToTopOfParent()
                    bottomToBottomOfParent()
                    setMargins(16.dp, topMargin, rightMargin, bottomMargin)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    theme {
                        imageTintList = ColorStateList.valueOf(foregroundColor)
                    }
                    setImageResource(this@SimpleItemView.option.icon)
                }})
                TextView({ constraintLayoutParams {
                    leftToRightOf(icon.id)
                    rightToRightOfParent()
                    topToTopOfParent()
                    bottomToBottomOfParent()
                    setMargins(16.dp, topMargin, 16.dp, bottomMargin)
                    gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                    textSize = 16F
                    theme {
                        setTextColor(foregroundColor)
                    }
                    setText(this@SimpleItemView.option.name)
                }})
            }

            override fun onChanged(value: MainNavOption.OptionID) {
                if (value == this@SimpleItemView.option.id) {
                    mask.isVisible = true
                    icon.isSelected = true
                } else {
                    mask.isVisible = false
                    icon.isSelected = false
                }
            }
        }
    }
}