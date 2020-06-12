package com.xkf.ppjoke.ui.home

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.fastjson.JSONObject
import com.xkf.libcommon.AppGlobal
import com.xkf.libnetwork.common.ApiResponse
import com.xkf.libnetwork.common.JsonCallback
import com.xkf.libnetwork.request.ApiService
import com.xkf.ppjoke.model.Feed
import com.xkf.ppjoke.model.User
import com.xkf.ppjoke.ui.login.UserManager

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