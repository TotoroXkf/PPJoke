package com.xkf.ppjoke.base

import android.app.Application
import com.xkf.libcommon.AppGlobal
import com.xkf.libnetwork.common.JsonConvert
import com.xkf.libnetwork.request.ApiService

/**
 * author : xiakaifa
 * 2020/5/15
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppGlobal.setApplication(this)
        AppConfig.init()
        ApiService.init(
            "http://123.56.232.18:8080/serverdemo/",
            JsonConvert()
        )
    }
}