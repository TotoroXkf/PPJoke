package com.xkf.libnetwork

import java.net.URLEncoder


object UrlCreator {
    fun createUrlFromParam(url: String, params: Map<String, Any>): String {
        val stringBuilder = StringBuilder(url)
        if (url.indexOf("?") > 0 || url.indexOf("&") > 0) {
            stringBuilder.append("&")
        } else {
            stringBuilder.append("?")
        }
        for ((key, value) in params) {
            val urlValue = URLEncoder.encode(value.toString(), "UTF-8")
            stringBuilder.append(key)
            stringBuilder.append("=")
            stringBuilder.append(value)
            stringBuilder.append("&")
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndex)
        return stringBuilder.toString()
    }
}