package gadget.component.main

import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.drawerlayout.widget.DrawerLayout
import gadget.basic.annotation.DslScope
import gadget.basic.theme.ThemeManager
import gadget.basic.theme.subscribeTheme
import gadget.basic.theme.theme
import gadget.basic.tool.dp
import gadget.basic.ui.dsl.ConstraintLayout
import gadget.basic.ui.dsl.ConstraintLayoutParams
import gadget.basic.ui.dsl.DrawerLayoutParams
import gadget.basic.ui.dsl.FrameLayout
import gadget.basic.ui.dsl.View
import gadget.basic.ui.dsl.ViewId
import gadget.basic.ui.dsl.bottomToBottomOfParent
import gadget.basic.ui.dsl.leftToLeftOfParent
import gadget.basic.ui.dsl.leftToRightOf
import gadget.basic.ui.dsl.marginLayoutParams
import gadget.basic.ui.dsl.onLayout
import gadget.basic.ui.dsl.rightToLeftOf
import gadget.basic.ui.dsl.rightToRightOfParent
import gadget.basic.ui.dsl.topToTopOfParent

@DslScope
class ComponentMainLayout(context: Context) : DrawerLayout(context) {

    init {
        marginLayoutParams {
            width = MATCH_PARENT
            height = MATCH_PARENT
        }
    }

    lateinit var sideContainer: FrameLayout
        private set

    lateinit var mainContainer: FrameLayout
        private set

    private val contentContainer = ConstraintLayout(DrawerLayoutParams(MATCH_PARENT to MATCH_PARENT) {
        it.subscribeTheme(ThemeManager.observable)
    }) {
        val (_sideContainer, _divider, _mainContainer) = ViewId

        this@ComponentMainLayout.sideContainer = FrameLayout(ConstraintLayoutParams { sideContainer ->
            leftToLeftOfParent()
            rightToLeftOf(_divider)
            topToTopOfParent()
            bottomToBottomOfParent()
            horizontalWeight = 618F
            horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
            sideContainer.id = _sideContainer
            sideContainer.theme {
                sideContainer.setBackgroundColor(primaryColor)
            }
        }) {
            setOnClickListener {
                ThemeManager.switch()
            }
        }

        View(ConstraintLayoutParams(1.dp to MATCH_CONSTRAINT) { divider ->
            leftToRightOf(_sideContainer)
            rightToLeftOf(_mainContainer)
            topToTopOfParent()
            bottomToBottomOfParent()
            divider.id = _divider
            divider.alpha = 0.618F
            divider.theme {
                divider.setBackgroundColor(backgroundColor)
            }
        })

        this@ComponentMainLayout.mainContainer = FrameLayout(ConstraintLayoutParams { mainContainer ->
            leftToRightOf(_divider)
            rightToRightOfParent()
            topToTopOfParent()
            bottomToBottomOfParent()
            horizontalWeight = 1000F
            horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
            mainContainer.id = _mainContainer
            mainContainer.theme {
                mainContainer.setBackgroundColor(errorColor)
            }
        }) {
        }

        onLayout { l1, t1, r1, b1, l2, t2, r2, b2 ->
        }
    }
}