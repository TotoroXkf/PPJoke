package com.xkf.ppjoke.exoplayer

import android.net.Uri
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Util
import com.xkf.libcommon.AppGlobal

object PageListPlayManager {
    private val pageListPlayHashMap = HashMap<String, PageListPlayer>()
    private val mediaSourceFactory: ProgressiveMediaSource.Factory

    init {
        val application = AppGlobal.getApplication()
        val dataSourceFactory =
            DefaultHttpDataSourceFactory(Util.getUserAgent(application, application.packageName))
        val cache =
            SimpleCache(application.cacheDir, LeastRecentlyUsedCacheEvictor(1024 * 1024 * 200))
        val cacheDataSinkFactory = CacheDataSinkFactory(cache, Long.MAX_VALUE)
        val cacheDataSourceFactory = CacheDataSourceFactory(
            cache,
            dataSourceFactory,
            FileDataSourceFactory(),
            cacheDataSinkFactory,
            CacheDataSource.FLAG_BLOCK_ON_CACHE,
            null
        )
        mediaSourceFactory = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
    }

    fun createMediaSource(url: String): MediaSource {
        return mediaSourceFactory.createMediaSource(Uri.parse(url))
    }

    fun get(pageName: String): PageListPlayer {
        var player = pageListPlayHashMap[pageName]
        if (player == null) {
            player = PageListPlayer()
            pageListPlayHashMap[pageName] = player
        }
        return player
    }

    fun release(pageName: String) {
        val player = pageListPlayHashMap.remove(pageName)
        player?.release()
    }
}