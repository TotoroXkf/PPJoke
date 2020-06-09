package com.xkf.ppjoke.ui.home

import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.xkf.libannotation.FragmentDestination
import com.xkf.ppjoke.base.AbstractListFragment
import com.xkf.ppjoke.model.Feed

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
class HomeFragment : AbstractListFragment<Feed, HomeViewModel>() {
    override fun afterCreateView() {
        viewModel.cacheLiveData.observe(this, Observer {
            pageListAdapter.submitList(it)
        })
    }
    
    override fun getAdapter(): PagedListAdapter<Feed, RecyclerView.ViewHolder> {
        return FeedAdapter("all")
    }
    
    override fun onRefresh(refreshLayout: RefreshLayout) {
        viewModel.dataSource.invalidate()
    }
    
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewBinding.refreshLayout.finishLoadMore()
    }
}