package com.xkf.ppjoke.ui.home

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.xkf.libannotation.FragmentDestination
import com.xkf.ppjoke.base.AbstractListFragment
import com.xkf.ppjoke.exoplayer.PageListPlayDetector
import com.xkf.ppjoke.exoplayer.PageListPlayManager
import com.xkf.ppjoke.model.Feed

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
class HomeFragment : AbstractListFragment<Feed, HomeViewModel>() {
    private lateinit var pageListPlayDetector: PageListPlayDetector
    private var feedType = "all"

    companion object {
        fun newInstance(feedType: String): HomeFragment {
            val args = Bundle()
            args.putString("feedType", feedType)
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun afterCreateView() {
        this.feedType = arguments?.getString("feedType") ?: "all"
        viewModel.feedType = feedType

        viewModel.cacheLiveData.observe(this, Observer {
            pageListAdapter.submitList(it)
        })

        pageListPlayDetector = PageListPlayDetector(this, viewBinding.recyclerView)
    }

    override fun getAdapter(): PagedListAdapter<Feed, RecyclerView.ViewHolder> {
        return object : FeedAdapter(feedType) {
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (this::pageListPlayDetector.isInitialized) {
            if (hidden) {
                pageListPlayDetector.onPause()
            } else {
                pageListPlayDetector.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        if (!this::pageListPlayDetector.isInitialized) {
            return
        }

        if (parentFragment != null) {
            if (requireParentFragment().isVisible && isVisible) {
                pageListPlayDetector.onPause()
            }
        } else {
            if (isVisible) {
                pageListPlayDetector.onPause()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (!this::pageListPlayDetector.isInitialized) {
            return
        }

        if (parentFragment != null) {
            if (requireParentFragment().isVisible && isVisible) {
                pageListPlayDetector.onResume()
            }
        } else {
            if (isVisible) {
                pageListPlayDetector.onResume()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        PageListPlayManager.release(feedType)
    }
}