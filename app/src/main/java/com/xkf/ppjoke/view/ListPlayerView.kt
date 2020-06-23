package com.xkf.ppjoke.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView
import com.xkf.libcommon.Utils
import com.xkf.ppjoke.R
import com.xkf.ppjoke.databinding.LayoutPlayerViewBinding
import com.xkf.ppjoke.exoplayer.IPlayTarget
import com.xkf.ppjoke.exoplayer.PageListPlayManager


class ListPlayerView : FrameLayout, IPlayTarget, PlayerControlView.VisibilityListener,
    Player.EventListener {
    private var category = ""
    private var videoUrl = ""
    private var viewBinding: LayoutPlayerViewBinding =
        LayoutPlayerViewBinding.inflate(LayoutInflater.from(context), this, true)
    private var isPlaying = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    fun bindData(
        category: String,
        widthPx: Int,
        heightPx: Int,
        coverUrl: String,
        videoUrl: String
    ) {
        this.category = category
        this.videoUrl = videoUrl
        setImageData(viewBinding.coverView, coverUrl, false)
        if (widthPx < heightPx) {
            viewBinding.blurBackgroundView.visibility = View.VISIBLE
            viewBinding.blurBackgroundView.setBlurImageView(coverUrl)
        } else {
            viewBinding.blurBackgroundView.visibility = View.GONE
        }
        setSize(widthPx, heightPx)
    }

    private fun setSize(widthPx: Int, heightPx: Int) {
        val maxWidth = Utils.getScreenWidthSize()
        val layoutHeight: Int
        val coverWidth: Int
        val coverHeight: Int
        if (widthPx >= heightPx) {
            coverWidth = maxWidth
            layoutHeight = (heightPx.toFloat() / (widthPx.toFloat() / maxWidth.toFloat())).toInt()
            coverHeight = layoutHeight
        } else {
            layoutHeight = maxWidth
            coverHeight = maxWidth
            coverWidth = (widthPx.toFloat() / (heightPx.toFloat() / maxWidth.toFloat())).toInt()
        }

        val layoutParams = layoutParams
        layoutParams.width = maxWidth
        layoutParams.height = layoutHeight
        setLayoutParams(layoutParams)

        val blurLayoutParams = viewBinding.blurBackgroundView.layoutParams
        layoutParams.width = maxWidth
        layoutParams.height = layoutHeight
        viewBinding.blurBackgroundView.layoutParams = blurLayoutParams

        val coverLayoutParams = viewBinding.coverView.layoutParams as LayoutParams
        coverLayoutParams.width = coverWidth
        coverLayoutParams.height = coverHeight
        coverLayoutParams.gravity = Gravity.CENTER
        viewBinding.coverView.layoutParams = coverLayoutParams
    }

    override fun getOwner(): ViewGroup {
        return this
    }

    override fun onActive() {
        val pageListPlayer = PageListPlayManager.get(category)
        val playerView = pageListPlayer.playerView
        val controlView = pageListPlayer.playerControlView
        val exoPlayer = pageListPlayer.exoPlayer
        if (playerView == null) {
            return
        }

        pageListPlayer.switchPlayerView(playerView, true)
        val playerViewParent = playerView.parent
        if (playerViewParent != this) {
            if (playerViewParent != null) {
                (playerViewParent as ViewGroup).removeView(playerView)
                (playerViewParent as ListPlayerView).inActive()
            }

            val layoutParams = viewBinding.coverView.layoutParams
            addView(playerView, 1, layoutParams)
        }

        val controlViewParent = controlView?.parent
        if (controlViewParent != this) {
            if (controlViewParent != null) {
                (controlViewParent as ViewGroup).removeView(controlView)
            }
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            layoutParams.gravity = Gravity.BOTTOM
            addView(controlView, layoutParams)
        }

        if (pageListPlayer.playUrl == videoUrl) {
            onPlayerStateChanged(true, Player.STATE_READY)
        } else {
            val mediaSource = PageListPlayManager.createMediaSource(videoUrl)
            exoPlayer?.prepare(mediaSource)
            exoPlayer?.repeatMode = Player.REPEAT_MODE_ONE
            pageListPlayer.playUrl = videoUrl
        }

        controlView?.show()
        controlView?.addVisibilityListener(this)
        exoPlayer?.addListener(this)
        exoPlayer?.playWhenReady = true

    }

    override fun inActive() {
        val pageListPlayer = PageListPlayManager.get(category)
        if (pageListPlayer.exoPlayer == null || pageListPlayer.playerControlView == null || pageListPlayer.playerView == null) {
            return
        }
        pageListPlayer.exoPlayer?.playWhenReady = false
        pageListPlayer.playerControlView?.removeVisibilityListener(this)
        pageListPlayer.exoPlayer?.removeListener(this)
        viewBinding.coverView.visibility = View.VISIBLE
        viewBinding.playBtnView.visibility = View.VISIBLE
        viewBinding.playBtnView.setImageResource(R.drawable.icon_video_play)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        isPlaying = false
        viewBinding.bufferView.visibility = View.GONE
        viewBinding.coverView.visibility = View.VISIBLE
        viewBinding.playBtnView.visibility = View.VISIBLE
        viewBinding.playBtnView.setImageResource(R.drawable.icon_video_play)
    }

    override fun isPlaying(): Boolean {
        return isPlaying
    }

    override fun onVisibilityChange(visibility: Int) {
        viewBinding.playBtnView.visibility = visibility
        if (isPlaying) {
            viewBinding.playBtnView.setImageResource(R.drawable.icon_video_pause)
        } else {
            viewBinding.playBtnView.setImageResource(R.drawable.icon_video_play)
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)

        val pageLisPlayer = PageListPlayManager.get(category)
        val exoPlayer = pageLisPlayer.exoPlayer ?: return
        if (playbackState == Player.STATE_READY && exoPlayer.bufferedPosition != 0L && playWhenReady) {
            viewBinding.coverView.visibility = View.GONE
            viewBinding.bufferView.visibility = View.GONE
        } else {
            viewBinding.bufferView.visibility = View.VISIBLE
        }

        isPlaying =
            playbackState == Player.STATE_READY && exoPlayer.bufferedPosition != 0L && playWhenReady
        viewBinding.playBtnView.setImageResource(if (isPlaying) R.drawable.icon_video_pause else R.drawable.icon_video_play)
    }

    fun getPlayControl(): View? {
        val pageLisPlayer = PageListPlayManager.get(category)
        return pageLisPlayer.playerControlView
    }
}