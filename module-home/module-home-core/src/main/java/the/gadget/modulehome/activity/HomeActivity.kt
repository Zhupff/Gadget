package the.gadget.modulehome.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import the.gadget.modulebase.activity.BindingActivity
import the.gadget.modulebase.fragment.BaseFragment
import the.gadget.modulebase.fragment.isAlive
import the.gadget.modulebase.weight.recyclerview.DiffFragmentStateAdapter
import the.gadget.modulehome.HomeApp
import the.gadget.modulehome.moduleHomeApi
import the.gadget.modulehomecore.databinding.HomeActivityBinding
import the.gadget.modulehomecore.R
import java.lang.ref.WeakReference

class HomeActivity : BindingActivity<HomeActivityBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.topBarLayout.binding.ivAppIcon.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(binding.sideBarLayout)) {
                binding.drawerLayout.closeDrawer(binding.sideBarLayout)
            } else {
                binding.drawerLayout.openDrawer(binding.sideBarLayout)
            }
        }

        binding.appFragmentPager.let { appFragmentPager ->
            appFragmentPager.isUserInputEnabled = false
            appFragmentPager.adapter = AppFragmentPagerAdapter(this, appFragmentPager)
        }

        moduleHomeApi.getAllHomeApps().observe(this) { allHomeApps ->
            allHomeApps?.find { it.selected }?.let { app ->
                (binding.appFragmentPager.adapter as AppFragmentPagerAdapter).openApp(app)
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.home_activity

    private class AppFragmentPagerAdapter(activity: HomeActivity, private val pager: ViewPager2) : DiffFragmentStateAdapter<HomeApp>(activity) {

        private val openedFragments: MutableMap<String, WeakReference<BaseFragment>> = mutableMapOf()

        override fun getItemCount(): Int = snapshot.size

        override fun createFragment(position: Int): Fragment {
            val app = data[position]
            var fragment = openedFragments[app.id]?.get()
            if (!fragment.isAlive()) {
                fragment = app.newFragment()
                openedFragments[app.id] = WeakReference(fragment)
            }
            return fragment!!
        }

        fun openApp(app: HomeApp) {
            val newData = if (data.contains(app)) data else data + app
            submit(newData)
            pager.setCurrentItem(newData.indexOf(app), false)
        }
    }
}