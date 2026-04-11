package gadget.component.main

import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.drawerlayout.widget.DrawerLayout
import gadget.basic.annotation.DslScope
import gadget.basic.theme.ThemeManager
import gadget.basic.theme.subscribeTheme
import gadget.basic.theme.theme
import gadget.basic.tool.dp
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

@DslScope
class ComponentMainLayout(context: Context) : DrawerLayout(context) {

    private val scope = scope({ marginLayoutParams(MATCH_PARENT, MATCH_PARENT) {
        subscribeTheme(ThemeManager.observable)
    }}) {

        ConstraintLayout({ drawerLayoutParams(MATCH_PARENT, MATCH_PARENT) }) {
            val (_sideContainer, _divider, _mainContainer) = ViewId

            FrameLayout({ constraintLayoutParams {
                id = _sideContainer
                leftToLeftOfParent()
                rightToLeftOf(_divider)
                topToTopOfParent()
                bottomToBottomOfParent()
                horizontalWeight = 618F
                horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
                theme {
                    setBackgroundColor(primaryColor)
                }
                setOnClickListener {
                    ThemeManager.switch()
                }
            }}) {
            }

            View({ constraintLayoutParams(1.dp, MATCH_CONSTRAINT) {
                id = _divider
                leftToRightOf(_sideContainer)
                rightToLeftOf(_mainContainer)
                topToTopOfParent()
                bottomToBottomOfParent()
                alpha = 0.618F
                theme {
                    setBackgroundColor(backgroundColor)
                }
            }})

            FrameLayout({ constraintLayoutParams {
                id = _mainContainer
                leftToRightOf(_divider)
                rightToRightOfParent()
                topToTopOfParent()
                bottomToBottomOfParent()
                horizontalWeight = 1000F
                horizontalChainStyle = ConstraintLayout.LayoutParams.CHAIN_PACKED
                theme {
                    setBackgroundColor(errorColor)
                }
            }}) {
            }
        }
    }
}