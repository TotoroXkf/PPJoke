package com.xkf.libnetwork

import com.alibaba.fastjson.JSON
import java.lang.reflect.Type

class JsonConvert : Convert {
    override fun convert(content: String, type: Type): Any? {
        val jsonObject = JSON.parseObject(content)
        val data = jsonObject.getJSONObject("data")
        if (data != null) {
            val data1 = data["data"]
            return JSON.parseObject(data1.toString(), type)
        }
        return null
    }
    
    override fun convert(content: String, clazz: Class<Any>): Any? {
        val jsonObject = JSON.parseObject(content)
        val data = jsonObject.getJSONObject("data")
        if (data != null) {
            val data1 = data["data"]
            return JSON.parseObject(data1.toString(), clazz)
        }
        return null
    }
}