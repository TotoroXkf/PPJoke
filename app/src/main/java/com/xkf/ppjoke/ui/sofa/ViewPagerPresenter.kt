package com.xkf.ppjoke.ui.sofa

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.xkf.ppjoke.databinding.FragmentSofaBinding
import com.xkf.ppjoke.ui.home.HomeFragment

class ViewPagerPresenter(
    val context: Context,
    private val viewBinding: FragmentSofaBinding,
    val tabPresenter: TabPresenter
) {
    private val pageChangeCallBack = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            val tabCount = viewBinding.tabLayout.tabCount
            for (i in 0 until tabCount) {
                val tabView = viewBinding.tabLayout.getTabAt(i)
                val customView = tabView?.customView as? TextView
                customView?.let {
                    if (tabView.position == position) {
                        customView.textSize = tabPresenter.getTabConfig().activeSize.toFloat()
                        customView.typeface = Typeface.DEFAULT_BOLD
                    } else {
                        customView.textSize = tabPresenter.getTabConfig().normalSize.toFloat()
                        customView.typeface = Typeface.DEFAULT
                    }
                }
            }
        }
    }

    fun setupViewPager(fragmentManager: FragmentManager, lifecycle: Lifecycle) {
        viewBinding.viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        viewBinding.viewPager.adapter = object : FragmentStateAdapter(fragmentManager, lifecycle) {
            override fun getItemCount(): Int = tabPresenter.getTabSize()

            override fun createFragment(position: Int): Fragment = getTabFragment(position)
        }
        tabPresenter.attachToViewPager()
        viewBinding.viewPager.registerOnPageChangeCallback(pageChangeCallBack)
        viewBinding.viewPager.post {
            viewBinding.viewPager.currentItem = tabPresenter.getTabConfig().select
        }
    }

    private fun getTabFragment(position: Int): Fragment {
        return HomeFragment.newInstance(tabPresenter.getTabs()[position].tag)
    }
}