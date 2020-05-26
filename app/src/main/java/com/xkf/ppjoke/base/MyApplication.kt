package com.xkf.ppjoke.base

import android.app.Application
import com.xkf.libcommon.AppGlobal
import com.xkf.libnetwork.ApiService
import com.xkf.libnetwork.JsonConvert

/**
 * author : xiakaifa
 * 2020/5/15
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        AppGlobal.setApplication(this)
        AppConfig.init()
        ApiService.init("", JsonConvert())
    }
}