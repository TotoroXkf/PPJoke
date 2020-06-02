package com.xkf.ppjoke.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.xkf.ppjoke.R
import com.xkf.ppjoke.databinding.LayoutRefreshViewBinding
import java.lang.reflect.ParameterizedType


abstract class AbstractListFragment<T, M : AbstractViewModel<T>> : Fragment(), OnRefreshListener,
    OnLoadMoreListener {
    lateinit var viewBinding: LayoutRefreshViewBinding
    private lateinit var adapter: PagedListAdapter<T, RecyclerView.ViewHolder>
    lateinit var viewModel: M
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = LayoutRefreshViewBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewBinding.refreshLayout.setEnableRefresh(true)
        viewBinding.refreshLayout.setEnableLoadMore(true)
        viewBinding.refreshLayout.setOnRefreshListener(this)
        viewBinding.refreshLayout.setOnLoadMoreListener(this)
        
        adapter = getAdapter()
        viewBinding.recyclerView.adapter = adapter
        val itemDecoration = DividerItemDecoration(activity, LinearLayoutManager.HORIZONTAL)
        itemDecoration.setDrawable(requireActivity().getDrawable(R.drawable.list_divider)!!)
        viewBinding.recyclerView.addItemDecoration(itemDecoration)
        viewBinding.recyclerView.itemAnimator = null
        
        val type = javaClass.genericSuperclass as ParameterizedType
        val arguments = type.actualTypeArguments
        if (arguments.size > 1) {
            val argument = arguments[1]
            val modelClass = (argument as Class<*>).asSubclass(AbstractViewModel::class.java)
            viewModel = ViewModelProvider(this).get(modelClass) as M
            viewModel.pageLiveData.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
            viewModel.boundaryPageData.observe(viewLifecycleOwner, Observer {
                finishRefresh(it)
            })
        }
        
        afterCreateView()
    }
    
    abstract fun afterCreateView()
    
    abstract fun getAdapter(): PagedListAdapter<T, RecyclerView.ViewHolder>
    
    fun submit(pageList: PagedList<T>) {
        if (pageList.isNotEmpty()) {
            adapter.submitList(pageList)
        }
        finishRefresh(pageList.size > 0)
    }
    
    private fun finishRefresh(hasData: Boolean) {
        val currentList = adapter.currentList
        
        val state = viewBinding.refreshLayout.state
        Log.e("xkf", "finishRefresh: $state")
        if (state.isFooter && state.isOpening) {
            viewBinding.refreshLayout.finishLoadMore()
        } else if (state.isHeader && state.isOpening) {
            viewBinding.refreshLayout.finishRefresh()
        }
        
        val finalHasData = hasData || currentList != null && currentList.isNotEmpty()
        if (finalHasData) {
            viewBinding.emptyView.visibility = View.GONE
        } else {
            viewBinding.emptyView.visibility = View.VISIBLE
        }
    }
}