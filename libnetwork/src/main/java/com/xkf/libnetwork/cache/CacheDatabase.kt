package com.xkf.libnetwork.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.xkf.libcommon.AppGlobal

@Database(entities = [Cache::class], version = 1)
abstract class CacheDatabase : RoomDatabase() {
    companion object {
        val database = Room.databaseBuilder(
            AppGlobal.getApplication().applicationContext,
            CacheDatabase::class.java,
            "cacheDatabase"
        ).allowMainThreadQueries().build()
    }
    
    public abstract fun cacheDao(): CacheDao
}
