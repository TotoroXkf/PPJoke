package com.xkf.ppjoke.ui.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xkf.libnetwork.cache.CacheManager
import com.xkf.ppjoke.model.User

object UserManager {
    private const val KEY_CACHE_USER = "cache_user"
    private var user: User? = null
    private val userLiveData = MutableLiveData<User>()

    init {
        val user = CacheManager.getCache(KEY_CACHE_USER) as? User
        if (user != null && user.expires_time > System.currentTimeMillis()) {
            this.user = user
        }
    }

    fun save(user: User) {
        CacheManager.saveCache(KEY_CACHE_USER, user)
        this.user = user
        if (userLiveData.hasActiveObservers()) {
            userLiveData.postValue(user)
        }
    }

    fun login(context: Context): LiveData<User> {
        context.startActivity(Intent(context, LoginActivity::class.java))
        return userLiveData
    }

    fun isLogin(): Boolean {
        if (user == null) {
            return false
        }
        return user?.expires_time ?: 0 > System.currentTimeMillis()
    }

    fun getUser(): User? {
        if (!isLogin()) {
            return null
        }
        return user
    }

    fun getUserId(): Long {
        if (!isLogin()) {
            return 0
        }
        return user?.userId ?: 0
    }
}