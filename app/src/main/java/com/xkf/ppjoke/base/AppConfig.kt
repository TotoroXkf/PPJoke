package com.xkf.ppjoke.base

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.xkf.libcommon.AppGlobal
import com.xkf.ppjoke.model.BottomBar
import com.xkf.ppjoke.model.Destination
import java.io.BufferedReader
import java.io.InputStreamReader

object AppConfig {
    fun init() {
        destinationMap = hashMapOf()
        var content =
            parseFile("destination.json")
        destinationMap.putAll(
            JSON.parseObject(
                content,
                object : TypeReference<HashMap<String, Destination>>() {}.type
            )
        )
        
        content =
            parseFile("main_tabs_config.json")
        bottomBar = JSON.parseObject(content, BottomBar::class.java)
    }
    
    lateinit var destinationMap: HashMap<String, Destination>
    
    lateinit var bottomBar: BottomBar
    
    private fun parseFile(fileName: String): String {
        val assets = AppGlobal.getApplication().assets
        val stream = assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(stream))
        val stringBuilder = StringBuilder()
        var line = reader.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = reader.readLine()
        }
        stream.close()
        reader.close()
        return stringBuilder.toString()
    }
}