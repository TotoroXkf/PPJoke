package com.xkf.ppjoke.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.xkf.libcommon.Utils
import com.xkf.ppjoke.databinding.LayoutPlayerViewBinding

class ListPlayerView : FrameLayout {
    private var category = ""
    private var videoUrl = ""
    private var layoutPlayerViewBinding: LayoutPlayerViewBinding =
        LayoutPlayerViewBinding.inflate(LayoutInflater.from(context), this, true)
    
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
        setImageData(layoutPlayerViewBinding.coverView, coverUrl, false)
        if (widthPx < heightPx) {
            layoutPlayerViewBinding.blurBackgroundView.visibility = View.VISIBLE
            layoutPlayerViewBinding.blurBackgroundView.setBlurImageView(coverUrl)
        } else {
            layoutPlayerViewBinding.blurBackgroundView.visibility = View.GONE
        }
        setSize(widthPx, heightPx)
    }
    
    private fun setSize(widthPx: Int, heightPx: Int) {
        val maxWidth = Utils.getScreenWidthSize()
        var layoutHeight = 0
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
        
        val blurLayoutParams = layoutPlayerViewBinding.blurBackgroundView.layoutParams
        layoutParams.width = maxWidth
        layoutParams.height = layoutHeight
        layoutPlayerViewBinding.blurBackgroundView.layoutParams = blurLayoutParams
        
        val coverLayoutParams = layoutPlayerViewBinding.coverView.layoutParams as LayoutParams
        coverLayoutParams.width = coverWidth
        coverLayoutParams.height = coverHeight
        coverLayoutParams.gravity = Gravity.CENTER
        layoutPlayerViewBinding.coverView.layoutParams = coverLayoutParams
        
        val playBtnLayoutParams =
            layoutPlayerViewBinding.blurBackgroundView.layoutParams as LayoutParams
        playBtnLayoutParams.gravity = Gravity.CENTER
        layoutPlayerViewBinding.playBtnView.layoutParams = playBtnLayoutParams
    }
}