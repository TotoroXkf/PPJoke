package com.xkf.ppjoke.base

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.xkf.libcommon.AppGlobal
import com.xkf.ppjoke.model.BottomBar
import com.xkf.ppjoke.model.Destination
import com.xkf.ppjoke.model.SofaTab
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

object AppConfig {
    lateinit var destinationMap: HashMap<String, Destination>
    lateinit var bottomBar: BottomBar
    lateinit var sofaTab: SofaTab

    fun init() {
        destinationMap = hashMapOf()
        var content = parseFile("destination.json")
        destinationMap.putAll(
            JSON.parseObject(
                content,
                object : TypeReference<HashMap<String, Destination>>() {}.type
            )
        )

        content = parseFile("main_tabs_config.json")
        bottomBar = JSON.parseObject(content, BottomBar::class.java)

        content = parseFile("sofa_tabs_config.json")
        sofaTab = JSONObject.parseObject(content, SofaTab::class.java)
        sofaTab.tabs.sortWith(Comparator { o1, o2 ->
            if (o1.index < o2.index) -1 else 1
        })
    }

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