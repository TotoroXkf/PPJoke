package com.xkf.libnetwork

import okhttp3.FormBody

class PostRequest<T>(url: String) : Request<T, PostRequest<T>>(url) {
    override fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request {
        val bodyBuilder = FormBody.Builder()
        for ((key, value) in params) {
            bodyBuilder.add(key, value.toString())
        }
        return builder.url(url).post(bodyBuilder.build()).build()
    }
}