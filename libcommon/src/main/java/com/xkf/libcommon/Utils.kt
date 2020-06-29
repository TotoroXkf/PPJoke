package com.xkf.libcommon

import android.content.res.Resources
import java.util.*
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

    fun calculateTime(time: Long): String {
        val timeInMillis = Calendar.getInstance().timeInMillis
        var finalTime = time
        if (time.toString().length < 13) {
            finalTime = time * 1000
        }
        val diff = (timeInMillis - finalTime) / 1000
        return when {
            diff <= 5 -> {
                "刚刚"
            }
            diff < 60 -> {
                diff.toString() + "秒前"
            }
            diff < 3600 -> {
                (diff / 60).toString() + "分钟前"
            }
            diff < 3600 * 24 -> {
                return (diff / 3600).toString() + "小时前"
            }
            else -> {
                (diff / (3600 * 24)).toString() + "天前"
            }
        }
    }
}