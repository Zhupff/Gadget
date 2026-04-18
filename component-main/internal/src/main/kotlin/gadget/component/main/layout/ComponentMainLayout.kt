package gadget.component.main.layout

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.drawerlayout.widget.DrawerLayout
import gadget.basic.annotation.DslScope
import gadget.basic.theme.GlobalTheme
import gadget.basic.theme.subscribeTheme
import gadget.basic.theme.theme
import gadget.basic.tool.dp
import gadget.basic.ui.common.moveTo
import gadget.basic.ui.dsl.ConstraintLayout
import gadget.basic.ui.dsl.FrameLayout
import gadget.basic.ui.dsl.View
import gadget.basic.ui.dsl.ViewId
import gadget.basic.ui.dsl.bottomToBottomOfParent
import gadget.basic.ui.dsl.constraintLayoutParams
import gadget.basic.ui.dsl.drawerLayoutParams
import gadget.basic.ui.dsl.leftToLeftOfParent
import gadget.basic.ui.dsl.leftToRightOf
import gadget.basic.ui.dsl.marginLayoutParams
import gadget.basic.ui.dsl.rightToLeftOf
import gadget.basic.ui.dsl.rightToRightOfParent
import gadget.basic.ui.dsl.scope
import gadget.basic.ui.dsl.topToTopOfParent
import gadget.component.main.ComponentMainActivity
import kotlin.math.max
import kotlin.math.min

@DslScope
class ComponentMainLayout(
    private val activity: ComponentMainActivity,
) : DrawerLayout(activity) {

    lateinit var backgroundContainer: FrameLayout
        private set

    lateinit var sideContainer: FrameLayout
        private set

    lateinit var divider: View
        private set

    lateinit var mainContainer: FrameLayout
        private set

    lateinit var drawer: FrameLayout
        private set

    private val scope = scope({ marginLayoutParams {
        subscribeTheme(GlobalTheme.current) {
            setScrimColor(backgroundColor and 0x00FFFFFF or 0x99000000.toInt())
        }
    }}) {

        this@ComponentMainLayout.backgroundContainer = FrameLayout({ marginLayoutParams {
            id = generateViewId()
        }})

        ConstraintLayout {
            val (_sideContainer, _divider, _mainContainer) = ViewId

            this@ComponentMainLayout.sideContainer = FrameLayout({ constraintLayoutParams {
                id = _sideContainer
                leftToLeftOfParent()
                rightToLeftOf(_divider)
                topToTopOfParent()
                bottomToBottomOfParent()
                horizontalWeight = 618F
                horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
            }})

            this@ComponentMainLayout.divider = View({ constraintLayoutParams(1.dp, MATCH_CONSTRAINT) {
                id = _divider
                leftToRightOf(_sideContainer)
                rightToLeftOf(_mainContainer)
                topToTopOfParent()
                bottomToBottomOfParent()
                alpha = 0.618F
                theme {
                    setBackgroundColor(outlineColor)
                }
            }})

            this@ComponentMainLayout.mainContainer = FrameLayout({ constraintLayoutParams {
                id = _mainContainer
                leftToRightOf(_divider)
                rightToRightOfParent()
                topToTopOfParent()
                bottomToBottomOfParent()
                horizontalWeight = 1000F
                horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
            }})
        }

        this@ComponentMainLayout.drawer = FrameLayout({ drawerLayoutParams {
            gravity = Gravity.LEFT
        }})
    }

    private val sideLayout: SideLayout by lazy { SideLayout(activity) }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            val w = r - l
            val h = b - t
            closeDrawer(drawer, false)
            if (max(w, h).toFloat() / min(w, h).toFloat() > 4F / 3F && h >= w) {
                sideContainer.visibility = GONE
                divider.visibility = GONE
                mainContainer.constraintLayoutParams {
                    leftToLeftOfParent()
                }
                drawer.visibility = VISIBLE
                sideLayout.moveTo(drawer)
                setDrawerLockMode(LOCK_MODE_UNLOCKED, drawer)
            } else {
                sideContainer.visibility = VISIBLE
                divider.visibility = VISIBLE
                mainContainer.constraintLayoutParams {
                    leftToRightOf(divider.id)
                }
                drawer.visibility = INVISIBLE
                sideLayout.moveTo(sideContainer)
                setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED, drawer)
            }
        }
    }
}