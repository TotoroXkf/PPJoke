package com.xkf.ppjoke.ui.home

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import com.alibaba.fastjson.TypeReference
import com.xkf.libnetwork.common.ApiResponse
import com.xkf.libnetwork.request.ApiService
import com.xkf.libnetwork.common.JsonCallback
import com.xkf.libnetwork.request.Request
import com.xkf.ppjoke.model.Feed
import com.xkf.ppjoke.ui.AbstractViewModel


class HomeViewModel : AbstractViewModel<Feed>() {
    @Volatile
    var withCache = true
    
    override fun createDataSource(): DataSource<Int, Feed> {
        return itemKeyedDataSource
    }
    
    private val itemKeyedDataSource = object : ItemKeyedDataSource<Int, Feed>() {
        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Feed>
        ) {
            loadData(0, callback)
            withCache = false
        }
        
        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            loadData(params.key, callback)
        }
        
        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Feed>) {
            callback.onResult(emptyList())
        }
        
        override fun getKey(item: Feed): Int {
            return item.id
        }
    }
    
    private fun loadData(key: Int, callback: ItemKeyedDataSource.LoadCallback<Feed>) {
        if (withCache) {
            val request = ApiService.get<List<Feed>>("feeds/queryHotFeedsList")
//                .addParam("feedType", 0)
                .addParam("userId", 0)
                .addParam("feedId", key)
                .addParam("pageCount", 10)
            request.type = object : TypeReference<List<Feed>>() {}.type
            
            request.cacheStrategy = Request.CACHE_ONLY
            request.execute(object : JsonCallback<List<Feed>>() {
                override fun onCacheSuccess(response: ApiResponse<List<Feed>>) {
                    Log.e("xkf", "onCacheSuccess: " + response.body?.size)
                }
            })
        }
        val request = ApiService.get<List<Feed>>("feeds/queryHotFeedsList")
//            .addParam("feedType", 0)
            .addParam("userId", 0)
            .addParam("feedId", key)
            .addParam("pageCount", 10)
        request.type = object : TypeReference<List<Feed>>() {}.type
        if (key == 0) {
            request.cacheStrategy = Request.NET_CACHE
        } else {
            request.cacheStrategy = Request.NET_ONLY
        }
        val response = request.execute()
        val data = response.body ?: emptyList()
        callback.onResult(data)
        
        if (key > 0) {
            boundaryPageData.postValue(data.isNotEmpty())
        }
    }
}