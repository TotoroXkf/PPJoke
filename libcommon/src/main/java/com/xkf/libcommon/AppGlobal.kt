package com.xkf.libcommon

import android.app.Application

object AppGlobal {
    private var application: Application? = null
    
    fun getApplication(): Application {
        return application!!
    }
    
    fun setApplication(application: Application) {
        AppGlobal.application = application
    }
}