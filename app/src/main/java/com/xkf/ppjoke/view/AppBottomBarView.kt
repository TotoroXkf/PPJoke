package com.xkf.ppjoke.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.xkf.ppjoke.R
import com.xkf.ppjoke.base.AppConfig
import com.xkf.ppjoke.utils.Utils
import com.xkf.ppjoke.model.BottomBar


class AppBottomBarView : BottomNavigationView {
    private val icons = intArrayOf(
        R.drawable.icon_tab_home,
        R.drawable.icon_tab_sofa,
        R.drawable.icon_tab_publish,
        R.drawable.icon_tab_find,
        R.drawable.icon_tab_mine
    )
    
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    
    init {
        val bottomBar = AppConfig.bottomBar
        setColors(bottomBar)
        addTabs(bottomBar)
    }
    
    @SuppressLint("RestrictedApi")
    private fun addTabs(bottomBar: BottomBar) {
        val tabs = bottomBar.tabs
        for (tab in tabs) {
            if (!tab.enable) {
                continue
            }
            val id = AppConfig.destinationMap[tab.pageUrl]?.id ?: 0
            if (id == 0) {
                continue
            }
            val menuItem = menu.add(0, id, tab.index, tab.title)
            menuItem.setIcon(icons[tab.index])
        }
        
        for (tab in tabs) {
            val iconSize = Utils.dpToPx(tab.size)
            val menuView = getChildAt(0) as? BottomNavigationMenuView
            menuView?.apply {
                val itemView = getChildAt(tab.index) as? BottomNavigationItemView
                itemView?.apply {
                    this.setIconSize(iconSize)
                    
                    if (TextUtils.isEmpty(tab.title)) {
                        setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.tintColor)))
                        setShifting(false)
                    }
                }
            }
        }
    }
    
    private fun setColors(bottomBar: BottomBar) {
        val stateList = arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf()
        )
        val colors = intArrayOf(
            Color.parseColor(bottomBar.activeColor),
            Color.parseColor(bottomBar.inActiveColor)
        )
        val colorStateList = ColorStateList(stateList, colors)
        itemIconTintList = colorStateList
        itemTextColor = colorStateList
        labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        selectedItemId = bottomBar.selectTab
    }
}