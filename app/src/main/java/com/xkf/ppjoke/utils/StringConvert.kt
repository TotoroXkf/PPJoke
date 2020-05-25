package com.xkf.ppjoke.utils


object StringConvert {
    fun convertFeedUgc(count: Int): String {
        if (count < 10000) {
            return count.toString()
        }
        return (count / 10000).toString() + "万"
    }
}