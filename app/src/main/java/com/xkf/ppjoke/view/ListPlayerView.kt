package com.xkf.ppjoke.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.xkf.ppjoke.databinding.LayoutPlayerViewBinding
import com.xkf.ppjoke.utils.Utils

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
        
        PPImageView.setImageData(layoutPlayerViewBinding.coverView, coverUrl, false)
        if (widthPx < heightPx) {
            layoutPlayerViewBinding.blurBackgroundView.visibility = View.VISIBLE
            layoutPlayerViewBinding.blurBackgroundView.setBlurImageView(coverUrl)
        } else {
            layoutPlayerViewBinding.blurBackgroundView.visibility = View.GONE
        }
    }
    
    fun setSize(widthPx: Int, heightPx: Int) {
        val maxWidth = Utils.getScreenWidthSize()
        val maxHeight = maxWidth
        val layoutWidth = maxWidth
        var layoutHeight = 0
        val coverWidth: Int
        val coverHeight: Int
        if (widthPx >= heightPx) {
            coverWidth = maxWidth
            layoutHeight = (heightPx.toFloat() / (widthPx.toFloat() / maxWidth.toFloat())).toInt()
            coverHeight = layoutHeight
        } else {
            layoutHeight = maxHeight
            coverHeight = maxHeight
            coverWidth = (widthPx.toFloat() / (heightPx.toFloat() / maxHeight.toFloat())).toInt()
        }
        
        val layoutParams = layoutParams
        layoutParams.width = layoutWidth
        layoutParams.height = layoutHeight
        setLayoutParams(layoutParams)
        
        val blurLayoutParams = layoutPlayerViewBinding.blurBackgroundView.layoutParams
        layoutParams.width = layoutWidth
        layoutParams.height = layoutHeight
        setLayoutParams(blurLayoutParams)
    }
}