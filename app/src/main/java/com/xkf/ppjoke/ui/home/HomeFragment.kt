package com.xkf.ppjoke.ui.home

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.xkf.libannotation.FragmentDestination
import com.xkf.ppjoke.model.Feed
import com.xkf.ppjoke.ui.AbstractListFragment

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
class HomeFragment : AbstractListFragment<Feed, HomeViewModel>() {
    override fun afterCreateView() {
    
    }
    
    override fun getAdapter(): PagedListAdapter<Feed, RecyclerView.ViewHolder> {
        return FeedAdapter("all")
    }
    
    override fun onRefresh(refreshLayout: RefreshLayout) {
    
    }
    
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        refreshLayout.finishLoadMore()
    }
}