package com.xkf.ppjoke.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

abstract class AbstractViewModel<T> : ViewModel() {
    private lateinit var dataSource: DataSource<Int, T>
    val pageLiveData: LiveData<PagedList<T>>
    val boundaryPageData = MutableLiveData<Boolean>()
    
    private val factory = object : DataSource.Factory<Int, T>() {
        override fun create(): DataSource<Int, T> {
            dataSource = createDataSource()
            return dataSource
        }
    }
    
    private val boundaryCallback = object : PagedList.BoundaryCallback<T>() {
        override fun onZeroItemsLoaded() {
            boundaryPageData.postValue(false)
        }
        
        override fun onItemAtEndLoaded(itemAtEnd: T) {
            boundaryPageData.postValue(true)
        }
    }
    
    init {
        val pageListConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(10)
            .build()
        pageLiveData = LivePagedListBuilder(factory, pageListConfig)
            .setInitialLoadKey(0)
            .setBoundaryCallback(boundaryCallback)
            .build()
    }
    
    
    abstract fun createDataSource(): DataSource<Int, T>
}