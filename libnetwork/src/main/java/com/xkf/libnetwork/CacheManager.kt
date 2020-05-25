package com.xkf.libnetwork

import com.xkf.libnetwork.cache.Cache
import com.xkf.libnetwork.cache.CacheDatabase
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


object CacheManager {
    fun <T> saveCache(key: String, body: T?) {
        val cache = Cache()
        cache.key = key
        cache.data = covert(body)
        CacheDatabase.database.cacheDao().save(cache)
    }
    
    fun getCache(key: String): Any? {
        val cache = CacheDatabase.database.cacheDao().getCache(key)
        return covert(cache?.data)
    }
    
    private fun covert(byteArray: ByteArray?): Any? {
        if (byteArray == null) {
            return null
        }
        val byteArrayInputStream = ByteArrayInputStream(byteArray)
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val result = objectInputStream.readObject()
        objectInputStream.close()
        byteArrayInputStream.close()
        return result
    }
    
    private fun <T> covert(body: T?): ByteArray {
        if (body == null) {
            return byteArrayOf()
        }
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objOutputStream.writeObject(body)
        objOutputStream.flush()
        objOutputStream.close()
        byteArrayOutputStream.close()
        return byteArrayOutputStream.toByteArray()
    }
}