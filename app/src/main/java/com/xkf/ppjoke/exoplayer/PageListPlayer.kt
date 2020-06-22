package com.xkf.ppjoke.exoplayer

import android.annotation.SuppressLint
import android.view.LayoutInflater
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.xkf.libcommon.AppGlobal
import com.xkf.ppjoke.R

@SuppressLint("InflateParams")
class PageListPlayer {
    var exoPlayer: SimpleExoPlayer?
    var playerView: PlayerView?
    var playerControlView: PlayerControlView?
    var playUrl: String = ""

    init {
        val application = AppGlobal.getApplication()

        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            application,
            DefaultRenderersFactory(application),
            DefaultTrackSelector(),
            DefaultLoadControl()
        )

        playerView = LayoutInflater.from(application).inflate(
            R.layout.layout_exo_player_view,
            null,
            false
        ) as PlayerView

        playerControlView = LayoutInflater.from(application).inflate(
            R.layout.layout_exo_player_contorller_view,
            null,
            false
        ) as PlayerControlView

        playerView?.player = exoPlayer
        playerControlView?.player = exoPlayer
    }

    fun release() {
        exoPlayer?.let {
            it.playWhenReady = false
            it.stop()
            it.release()
        }
        exoPlayer = null

        playerView?.let {
            it.player = null
        }
        playerView = null

        playerControlView?.let {
            it.player = null
//            it.removeVisibilityListener()
        }
        playerControlView = null
    }

    fun switchPlayerView(newPlayerView: PlayerView, attach: Boolean) {
        if (attach) {
            playerView?.player = null
            newPlayerView.player = exoPlayer
        } else {
            playerView?.player = exoPlayer
            newPlayerView.player = null
        }
    }
}