package com.xkf.ppjoke.ui.home

import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.xkf.libannotation.FragmentDestination
import com.xkf.ppjoke.base.AbstractListFragment
import com.xkf.ppjoke.exoplayer.PageListPlayDetector
import com.xkf.ppjoke.model.Feed

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
class HomeFragment : AbstractListFragment<Feed, HomeViewModel>() {
    private lateinit var pageListPlayDetector: PageListPlayDetector

    override fun afterCreateView() {
        viewModel.cacheLiveData.observe(this, Observer {
            pageListAdapter.submitList(it)
        })

        pageListPlayDetector = PageListPlayDetector(this, viewBinding.recyclerView)
    }

    override fun getAdapter(): PagedListAdapter<Feed, RecyclerView.ViewHolder> {
        return object : FeedAdapter("all") {
            override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
                super.onViewAttachedToWindow(holder)

                if (holder is FeedAdapter.ViewHolder) {
                    if (holder.isVideoItem() && this@HomeFragment::pageListPlayDetector.isInitialized) {
                        pageListPlayDetector.addTarget(holder.getListPlayerView()!!)
                    }
                }
            }

            override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
                super.onViewDetachedFromWindow(holder)

                if (holder is FeedAdapter.ViewHolder) {
                    if (holder.isVideoItem() && this@HomeFragment::pageListPlayDetector.isInitialized) {
                        pageListPlayDetector.removeTarget(holder.getListPlayerView()!!)
                    }
                }
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        viewModel.dataSource.invalidate()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        viewBinding.refreshLayout.finishLoadMore()
    }

    override fun onPause() {
        super.onPause()

        if (this::pageListPlayDetector.isInitialized) {
            pageListPlayDetector.onPause()
        }
    }

    override fun onResume() {
        super.onResume()

        if (this::pageListPlayDetector.isInitialized) {
            pageListPlayDetector.onResume()
        }
    }
}