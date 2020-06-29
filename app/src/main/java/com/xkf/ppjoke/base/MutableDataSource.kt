package com.xkf.ppjoke.base

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList

class MutableDataSource<Key, Value> : PageKeyedDataSource<Key, Value>() {
    val list = mutableListOf<Value>()

    override fun loadInitial(
        params: LoadInitialParams<Key>,
        callback: LoadInitialCallback<Key, Value>
    ) {
        callback.onResult(list, null, null)
    }

    override fun loadAfter(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        callback.onResult(emptyList(), null)
    }

    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        callback.onResult(emptyList(), null)
    }

    @SuppressLint("RestrictedApi")
    fun buildNewPageList(config: PagedList.Config): PagedList<Value> {
        return PagedList.Builder<Key, Value>(this, config)
            .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
            .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
            .build()
    }
}