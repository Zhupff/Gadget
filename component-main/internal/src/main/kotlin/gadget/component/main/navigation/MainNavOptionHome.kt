package gadget.component.main.navigation

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.auto.service.AutoService
import gadget.basic.theme.theme
import gadget.basic.tool.dp
import gadget.basic.ui.dsl.ImageView
import gadget.basic.ui.dsl.OnAttachStateChanged
import gadget.basic.ui.dsl.TextView
import gadget.basic.ui.dsl.bottomToBottomOfParent
import gadget.basic.ui.dsl.constraintLayoutParams
import gadget.basic.ui.dsl.leftToLeftOfParent
import gadget.basic.ui.dsl.leftToRightOf
import gadget.basic.ui.dsl.marginLayoutParams
import gadget.basic.ui.dsl.rightToRightOfParent
import gadget.basic.ui.dsl.scope
import gadget.basic.ui.dsl.topToTopOfParent
import gadget.basic.ui.view.GradientTransparentL2R
import gadget.component.main.internal.R

@AutoService(MainNavOption.Provider::class)
class MainNavOptionHomeProvider : MainNavOption.Provider {
    override fun provide(
        context: Context,
        selection: LiveData<MainNavOption.OptionID>
    ): MainNavOption = MainNavOptionHome(context, selection)
}

class MainNavOptionHome(
    private val context: Context,
    private val selection: LiveData<MainNavOption.OptionID>,
) : MainNavOption {

    override val id: MainNavOption.OptionID = MainNavOption.OptionID.HOME

    override val icon: Int = R.drawable.main_nav_option_home

    override val name: Int = R.string.main_nav_option_home

    override val view: View = ItemView()

    private inner class ItemView : ConstraintLayout(context), Observer<MainNavOption.OptionID> {
        private lateinit var mask: GradientTransparentL2R
        private lateinit var icon: ImageView
        private val scope = scope({ marginLayoutParams(MATCH_PARENT, 50.dp) {
            OnAttachStateChanged({
                selection.observeForever(this@ItemView)
            }, {
                selection.removeObserver(this@ItemView)
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
                setImageResource(this@MainNavOptionHome.icon)
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
                setText(this@MainNavOptionHome.name)
            }})
        }

        override fun onChanged(value: MainNavOption.OptionID) {
            if (value == this@MainNavOptionHome.id) {
                mask.isVisible = true
                icon.isSelected = true
            } else {
                mask.isVisible = false
                icon.isSelected = false
            }
        }
    }
}