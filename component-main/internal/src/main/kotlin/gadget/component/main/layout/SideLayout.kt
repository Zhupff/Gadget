package gadget.component.main.layout

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
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
import gadget.basic.ui.dsl.RecyclerView
import gadget.basic.ui.dsl.TextView
import gadget.basic.ui.dsl.Toolbar
import gadget.basic.ui.dsl.View
import gadget.basic.ui.dsl.ViewId
import gadget.basic.ui.dsl.appBarLayoutParams
import gadget.basic.ui.dsl.collapsingToolbarLayoutParams
import gadget.basic.ui.dsl.constraintLayoutParams
import gadget.basic.ui.dsl.coordinatorLayoutParams
import gadget.basic.ui.dsl.frameLayoutParams
import gadget.basic.ui.dsl.leftToLeftOfParent
import gadget.basic.ui.dsl.rightToRightOfParent
import gadget.basic.ui.dsl.scope
import gadget.basic.ui.dsl.toolbarLayoutParams
import gadget.basic.ui.dsl.topToBottomOf
import gadget.basic.ui.dsl.topToTopOfParent
import gadget.basic.ui.view.ConstraintLayoutX
import gadget.basic.ui.view.GravityImageView
import kotlin.math.absoluteValue

@DslScope
internal class SideLayout(context: Context) : FrameLayout(context) {

    private lateinit var backgroundMask: View

    private lateinit var photoLayout: View

    private lateinit var userLayout: View

    private val scope = scope {

        this@SideLayout.backgroundMask = View({ frameLayoutParams(MATCH_PARENT, MATCH_PARENT) {
            theme {
                setBackgroundColor(backgroundColor)
            }
        }})

        CoordinatorLayout {

            AppBarLayout({ coordinatorLayoutParams(MATCH_PARENT, WRAP_CONTENT) {
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
                            gravity = GravityX.T
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
            }}) {
            }
        }
    }
}