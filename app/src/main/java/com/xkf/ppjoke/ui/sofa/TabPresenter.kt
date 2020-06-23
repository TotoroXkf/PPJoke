package com.xkf.ppjoke.ui.sofa

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.TextView
import com.google.android.material.tabs.TabLayoutMediator
import com.xkf.ppjoke.base.AppConfig
import com.xkf.ppjoke.databinding.FragmentSofaBinding
import com.xkf.ppjoke.model.SofaTab


class TabPresenter(val context: Context, private val viewBinding: FragmentSofaBinding) {
    private val tabConfig = AppConfig.sofaTab
    private val tabs = arrayListOf<SofaTab.Tabs>()

    fun setupTabs() {
        for (tab in tabConfig.tabs) {
            if (tab.enable) {
                tabs.add(tab)
            }
        }
        viewBinding.tabLayout.tabGravity = tabConfig.tabGravity
    }

    private fun makeTabView(position: Int): TextView {
        val tabView = TextView(context)
        val states = Array<IntArray>(2) {
            if (it == 0) {
                intArrayOf(android.R.attr.state_selected)
            } else {
                intArrayOf()
            }
        }
        val colors = intArrayOf(
            Color.parseColor(tabConfig.activeColor),
            Color.parseColor(tabConfig.normalColor)
        )
        val colorStateList = ColorStateList(states, colors)
        tabView.setTextColor(colorStateList)
        tabView.text = tabs[position].title
        tabView.textSize = tabConfig.normalSize.toFloat()
        return tabView
    }

    fun getTabSize() = tabs.size

    fun getTabs(): ArrayList<SofaTab.Tabs> {
        return tabs
    }

    fun getTabConfig(): SofaTab {
        return tabConfig
    }

    fun attachToViewPager() {
        val tabLayoutMediator = TabLayoutMediator(
            viewBinding.tabLayout,
            viewBinding.viewPager,
            true,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.customView = makeTabView(position)
            })
        tabLayoutMediator.attach()
    }
}