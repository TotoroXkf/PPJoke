package com.xkf.libnetwork.cache

import androidx.room.*

@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(cache: Cache)
    
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(cache: Cache)
    
    @Delete
    fun delete(cache: Cache)
    
    @Query("select * from Cache where `key`= :key")
    fun getCache(key: String): Cache?
}
