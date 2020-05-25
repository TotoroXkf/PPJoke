package com.xkf.libnetwork


abstract class JsonCallback<T> {
    open fun onSuccess(response: ApiResponse<T>) {
    
    }
    
    open fun onError(response: ApiResponse<T>) {
    
    }
    
    open fun onCacheSuccess(response: ApiResponse<T>) {
        
    }
}