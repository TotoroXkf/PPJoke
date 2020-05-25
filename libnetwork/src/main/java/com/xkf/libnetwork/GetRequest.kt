package com.xkf.libnetwork

/**
 * author : xiakaifa
 * 2020/5/18
 */
class GetRequest<T>(url: String) : Request<T, GetRequest<T>>(url) {
    override fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request {
        return builder.get().url(UrlCreator.createUrlFromParam(url, params)).build()
    }
}