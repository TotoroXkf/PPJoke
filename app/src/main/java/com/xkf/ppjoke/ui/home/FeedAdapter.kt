package com.xkf.ppjoke.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xkf.ppjoke.databinding.LayoutFeedTypeImageBinding
import com.xkf.ppjoke.databinding.LayoutFeedTypeVideoBinding
import com.xkf.ppjoke.model.Feed
import com.xkf.ppjoke.view.ListPlayerView


/**
 * author : xiakaifa
 * 2020/5/28
 */
open class FeedAdapter(val category: String) :
    PagedListAdapter<Feed, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Feed>() {
        override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem == newItem
        }
    }) {

    companion object {
        const val TYPE_IMAGE = 1
        const val TYPE_VIDEO = 2
    }

    override fun getItemViewType(position: Int): Int {
        val feed = getItem(position)
        return feed?.itemType ?: TYPE_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewDataBinding: ViewDataBinding = if (viewType == TYPE_VIDEO) {
            LayoutFeedTypeVideoBinding.inflate(layoutInflater, parent, false)
        } else {
            LayoutFeedTypeImageBinding.inflate(layoutInflater, parent, false)
        }
        return ViewHolder(viewDataBinding.root, viewDataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindData(getItem(position)!!)
    }

    inner class ViewHolder(itemView: View, private val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(itemView) {

        fun bindData(feed: Feed) {
            if (viewDataBinding is LayoutFeedTypeImageBinding) {
                viewDataBinding.feed = feed
                viewDataBinding.lifecycleOwner = itemView.context as LifecycleOwner
                viewDataBinding.feedImage.bindData(
                    widthPx = feed.width,
                    heightPx = feed.height,
                    marginLeft = 16,
                    imageUrl = feed.cover ?: ""
                )
            } else if (viewDataBinding is LayoutFeedTypeVideoBinding) {
                viewDataBinding.feed = feed
                viewDataBinding.lifecycleOwner = itemView.context as LifecycleOwner
                viewDataBinding.listPlayerView.bindData(
                    category = category,
                    widthPx = feed.width,
                    heightPx = feed.height,
                    coverUrl = feed.cover,
                    videoUrl = feed.url
                )
            }
        }

        fun isVideoItem(): Boolean {
            return viewDataBinding is LayoutFeedTypeVideoBinding
        }

        fun getListPlayerView(): ListPlayerView? {
            if (viewDataBinding is LayoutFeedTypeVideoBinding) {
                return viewDataBinding.listPlayerView
            }
            return null
        }
    }
}
