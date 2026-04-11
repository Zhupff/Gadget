package gadget.component.main.layout

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import gadget.basic.annotation.DslScope
import gadget.basic.theme.theme
import gadget.basic.ui.dsl.View
import gadget.basic.ui.dsl.frameLayoutParams
import gadget.basic.ui.dsl.scope

@DslScope
class ComponentSideLayout(context: Context) : FrameLayout(context) {

    private lateinit var backgroundMask: View

    private val scope = scope {

        this@ComponentSideLayout.backgroundMask = View({ frameLayoutParams(MATCH_PARENT, MATCH_PARENT) {
            theme {
                setBackgroundColor(backgroundColor)
            }
        }})
    }
}