package com.xkf.ppjoke.ui.home

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.fastjson.JSONObject
import com.xkf.libcommon.AppGlobal
import com.xkf.libnetwork.common.ApiResponse
import com.xkf.libnetwork.common.JsonCallback
import com.xkf.libnetwork.request.ApiService
import com.xkf.ppjoke.model.Comment
import com.xkf.ppjoke.model.Feed
import com.xkf.ppjoke.model.User
import com.xkf.ppjoke.ui.login.UserManager
import com.xkf.ppjoke.view.ShareDialog
import java.util.*

fun toggleFeedLike(lifecycleOwner: LifecycleOwner, feed: Feed) {
    if (!UserManager.isLogin()) {
        val liveData = UserManager.login(AppGlobal.getApplication())
        liveData.observe(lifecycleOwner, object : Observer<User> {
            override fun onChanged(user: User?) {
                if (user != null) {
                    toggleFeedLikeInternal(feed)
                }
                liveData.removeObserver(this)
            }
        })
    }
    toggleFeedLikeInternal(feed)
}

private fun toggleFeedLikeInternal(feed: Feed) {
    ApiService.get<JSONObject>("ugc/toggleFeedLike")
        .addParam("userId", UserManager.getUserId())
        .addParam("itemId", feed.itemId)
        .execute(object : JsonCallback<JSONObject>() {
            override fun onSuccess(response: ApiResponse<JSONObject>) {
                if (response.body != null) {
                    val hasLiked = response.body!!.getBoolean("hasLiked")
                    feed.getUgc().isHasLiked = hasLiked
                }
            }
        })
}

fun toggleFeedDiss(lifecycleOwner: LifecycleOwner, feed: Feed) {
    if (!UserManager.isLogin()) {
        val liveData = UserManager.login(AppGlobal.getApplication())
        liveData.observe(lifecycleOwner, object : Observer<User> {
            override fun onChanged(user: User?) {
                if (user != null) {
                    toggleFeedDissInternal(feed)
                }
                liveData.removeObserver(this)
            }
        })
    }
    toggleFeedDissInternal(feed)
}

private fun toggleFeedDissInternal(feed: Feed) {
    ApiService.get<JSONObject>("ugc/dissFeed")
        .addParam("userId", UserManager.getUserId())
        .addParam("itemId", feed.itemId)
        .execute(object : JsonCallback<JSONObject>() {
            override fun onSuccess(response: ApiResponse<JSONObject>) {
                if (response.body != null) {
                    val hasLiked = response.body!!.getBoolean("hasLiked")
                    feed.getUgc().isHasdiss = hasLiked
                }
            }
        })
}

fun openShare(context: Context, feed: Feed) {
    val url =
        "http://h5.aliyun.ppjoke.com/item/${feed.itemId}?timestamp=${Date().time}&user_id=${UserManager.getUserId()}"
    val dialog = ShareDialog(context, url)
    dialog.onClickItemListener = {
        ApiService.get<JSONObject>("ugc/increaseShareCount")
            .addParam("itemId", feed.itemId)
            .execute(object : JsonCallback<JSONObject>() {
                override fun onSuccess(response: ApiResponse<JSONObject>) {
                    if (response.body != null) {
                        val count = response.body!!.getIntValue("count")
                        feed.ugc.setShareCount(count)
                    }
                }
            })
    }
    dialog.show()
}

fun toggleCommentLike(lifecycleOwner: LifecycleOwner, comment: Comment) {
    // ugc/toggleCommentLike
    if (!UserManager.isLogin()) {
        val liveData = UserManager.login(AppGlobal.getApplication())
        liveData.observe(lifecycleOwner, object : Observer<User> {
            override fun onChanged(user: User?) {
                if (user != null) {
                    toggleCommentLikeInternal(comment)
                }
                liveData.removeObserver(this)
            }
        })
    }
    toggleCommentLikeInternal(comment)
}

fun toggleCommentLikeInternal(comment: Comment) {
    ApiService.get<JSONObject>("ugc/toggleCommentLike")
        .addParam("commentId", comment.commentId)
        .addParam("userId", UserManager.getUserId())
        .execute(object : JsonCallback<JSONObject>() {
            override fun onSuccess(response: ApiResponse<JSONObject>) {
                if (response.body != null) {
                    val hasLiked = response.body!!.getBooleanValue("hasLiked")
                    comment.ugc.isHasLiked = hasLiked
                }
            }
        })

}