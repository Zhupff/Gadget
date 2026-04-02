package gadget.component.main

import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.drawerlayout.widget.DrawerLayout
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
import gadget.basic.ui.dsl.onLayout
import gadget.basic.ui.dsl.rightToLeftOf
import gadget.basic.ui.dsl.rightToRightOfParent
import gadget.basic.ui.dsl.topToTopOfParent

class ComponentMainLayout(context: Context) : DrawerLayout(context) {

    lateinit var sideContainer: FrameLayout
        private set

    lateinit var mainContainer: FrameLayout
        private set

    private val contentContainer = ConstraintLayout(DrawerLayoutParams(MATCH_PARENT to MATCH_PARENT) {
    }) {
        val (_sideContainer, _divider, _mainContainer) = ViewId

        sideContainer = FrameLayout(ConstraintLayoutParams { sideContainer ->
            sideContainer.id = _sideContainer
            leftToLeftOfParent()
            rightToLeftOf(_divider)
            topToTopOfParent()
            bottomToBottomOfParent()
            horizontalWeight = 0.618F
            horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
        }) {
        }

        View(ConstraintLayoutParams(1.dp to MATCH_CONSTRAINT) { divider ->
            divider.id = _divider
            leftToRightOf(_sideContainer)
            rightToLeftOf(_mainContainer)
            topToTopOfParent()
            bottomToBottomOfParent()
            divider.alpha = 0.618F
        })

        mainContainer = FrameLayout(ConstraintLayoutParams { mainContainer ->
            mainContainer.id = _mainContainer
            leftToRightOf(_divider)
            rightToRightOfParent()
            topToTopOfParent()
            bottomToBottomOfParent()
            horizontalWeight = 1F
            horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
        }) {
        }

        onLayout { l1, t1, r1, b1, l2, t2, r2, b2 ->
        }
    }
}