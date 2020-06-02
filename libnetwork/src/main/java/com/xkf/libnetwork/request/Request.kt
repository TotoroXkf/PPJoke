package com.xkf.libnetwork.request

import android.annotation.SuppressLint
import androidx.annotation.IntDef
import androidx.arch.core.executor.ArchTaskExecutor
import com.xkf.libnetwork.common.ApiResponse
import com.xkf.libnetwork.common.JsonCallback
import com.xkf.libnetwork.common.UrlCreator
import com.xkf.libnetwork.cache.CacheManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Suppress("UNCHECKED_CAST")
abstract class Request<T, R : Request<T, R>>(var url: String) {
    companion object {
        const val CACHE_ONLY = 1
        const val CACHE_FIRST = 2
        const val NET_ONLY = 3
        const val NET_CACHE = 4
    }
    
    val headers = hashMapOf<String, String>()
    val params = hashMapOf<String, Any>()
    
    var type: Type? = null
    var clazz: Class<Any>? = null
    var cacheKey: String = ""
    var cacheStrategy = 3
    
    fun addHeader(key: String, value: String): R {
        headers[key] = value
        return this as R
    }
    
    fun addParam(key: String, value: Any): R {
        val field = value::class.java.getDeclaredField("TYPE")
        val clazz: Class<*> = field.get(null) as Class<*>
        if (clazz.isPrimitive) {
            params[key] = value
        }
        return this as R
    }
    
    fun execute(): ApiResponse<T> {
        if (cacheStrategy == CACHE_ONLY) {
            val cache = readCache()
            if (cache != null) {
                return cache
            }
        }
        
        val response = getCall().execute()
        return parseResponse(response, null)
    }
    
    @SuppressLint("RestrictedApi")
    fun execute(callback: JsonCallback<T>?) {
        if (cacheStrategy != NET_ONLY) {
            ArchTaskExecutor.getIOThreadExecutor().execute {
                val cache = readCache()
                if (cache != null) {
                    callback?.onCacheSuccess(cache)
                }
            }
        }
        
        if (cacheStrategy != CACHE_ONLY) {
            getCall().enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val apiResponse = parseResponse(response, callback)
                    if (apiResponse.success) {
                        callback?.onSuccess(apiResponse)
                    } else {
                        callback?.onError(apiResponse)
                    }
                }
                
                override fun onFailure(call: Call, e: IOException) {
                    val apiResponse =
                        ApiResponse<T>()
                    apiResponse.message = e.message ?: ""
                    callback?.onError(apiResponse)
                }
            })
        }
    }
    
    private fun readCache(): ApiResponse<T>? {
        if (cacheKey.isEmpty()) {
            cacheKey =
                UrlCreator.createUrlFromParam(url, params)
        }
        val cache = CacheManager.getCache(cacheKey)
        if (cache != null) {
            val apiResponse = ApiResponse<T>()
            apiResponse.status = 304
            apiResponse.message = "读取缓存"
            apiResponse.success = true
            apiResponse.body = cache as T
            return apiResponse
        }
        return null
    }
    
    private fun parseResponse(
        response: Response,
        callback: JsonCallback<T>?
    ): ApiResponse<T> {
        val apiResponse = ApiResponse<T>()
        apiResponse.status = response.code
        apiResponse.success = response.isSuccessful
        if (response.isSuccessful) {
            val content = response.body!!.string()
            val convert = ApiService.convert
            when {
                type != null -> {
                    apiResponse.body = convert.convert(content, type!!) as T
                }
                clazz != null -> {
                    apiResponse.body = convert.convert(content, clazz!!) as T
                }
                callback != null -> {
                    apiResponse.body = convert.convert(content, getTypeFromCallback(callback)) as T
                }
            }
        } else {
            apiResponse.message = response.message
        }
        
        if (cacheStrategy != NET_ONLY && response.isSuccessful && apiResponse.body != null && apiResponse.body is Serializable) {
            if (cacheKey.isEmpty()) {
                cacheKey =
                    UrlCreator.createUrlFromParam(url, params)
            }
            CacheManager.saveCache(cacheKey, apiResponse.body)
        }
        return apiResponse
    }
    
    private fun getTypeFromCallback(callback: JsonCallback<T>): Type {
        val parameterizedType: ParameterizedType =
            callback::class.java.genericSuperclass as ParameterizedType
        return parameterizedType.actualTypeArguments[0]
    }
    
    private fun getCall(): Call {
        val builder = okhttp3.Request.Builder()
        for ((key, value) in headers) {
            builder.addHeader(key, value)
        }
        val request = generateRequest(builder)
        return ApiService.httpClient.newCall(request)
    }
    
    abstract fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request
    
    @IntDef(value = [CACHE_ONLY, CACHE_FIRST, NET_ONLY, NET_CACHE])
    annotation class CacheStrategy
}
