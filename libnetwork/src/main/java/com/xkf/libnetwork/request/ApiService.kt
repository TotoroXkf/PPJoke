package com.xkf.libnetwork.request

import android.annotation.SuppressLint
import com.xkf.libnetwork.common.Convert
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager


object ApiService {
    val httpClient: OkHttpClient
    lateinit var baseUrl: String
    lateinit var convert: Convert
    
    fun init(baseUrl: String, convert: Convert) {
        ApiService.baseUrl = baseUrl
        ApiService.convert = convert
    }
    
    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val trustManager = arrayOf(object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            
            }
            
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            
            }
            
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustManager, SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
    }
    
    fun <T> get(url: String): GetRequest<T> {
        return GetRequest(baseUrl + url)
    }
    
    fun <T> post(url: String): PostRequest<T> {
        return PostRequest(baseUrl + url)
    }
}