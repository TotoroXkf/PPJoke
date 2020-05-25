package com.xkf.ppjoke.utils

import android.content.res.Resources
import kotlin.math.floor

object Utils {
    fun dpToPx(dpValue: Int): Int {
        return floor(Resources.getSystem().displayMetrics.density * dpValue.toDouble()).toInt()
    }
    
    fun getScreenWidthSize(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }
    
    fun getScreenHeightSize(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}