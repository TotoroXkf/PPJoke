package com.xkf.ppjoke.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.xkf.ppjoke.utils.Utils
import jp.wasabeef.glide.transformations.BlurTransformation


class PPImageView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    
    companion object {
        @SuppressLint("CheckResult")
        @BindingAdapter(value = ["imageUrl", "isCircle"])
        fun setImageData(view: PPImageView, imageUrl: String, isCircle: Boolean) {
            val builder = Glide.with(view).load(imageUrl)
            if (isCircle) {
                builder.transform(CircleCrop())
            }
            val layoutParams = view.layoutParams
            if (layoutParams != null && layoutParams.width > 0 && layoutParams.height > 0) {
                builder.override(layoutParams.width, layoutParams.height)
            }
            builder.into(view)
        }
    }
    
    fun bindData(
        widthPx: Int,
        heightPx: Int,
        marginLeft: Int,
        maxWidth: Int = Utils.getScreenWidthSize(),
        maxHeight: Int = Utils.getScreenHeightSize(),
        imageUrl: String
    ) {
        if (widthPx <= 0 || heightPx <= 0) {
            Glide.with(this).load(imageUrl).into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    val height = resource.intrinsicHeight
                    val width = resource.intrinsicWidth
                    setSize(width, height, marginLeft, maxWidth, maxHeight)
                    setImageDrawable(resource)
                }
            })
            return
        }
        setSize(widthPx, heightPx, marginLeft, maxWidth, maxHeight)
        setImageData(this, imageUrl, false)
    }
    
    private fun setSize(width: Int, height: Int, marginLeft: Int, maxWidth: Int, maxHeight: Int) {
        val finalWidth: Int
        val finalHeight: Int
        // width/height = finalWidth/finalHeight
        if (width > height) {
            finalWidth = maxWidth
            finalHeight = (finalWidth.toFloat() * (height.toFloat() / width.toFloat())).toInt()
        } else {
            finalHeight = maxHeight
            finalWidth = (finalHeight.toFloat() * (width.toFloat() / height.toFloat())).toInt()
        }
        val layoutParams = ViewGroup.MarginLayoutParams(finalWidth, finalHeight)
        layoutParams.leftMargin = Utils.dpToPx(marginLeft)
        setLayoutParams(layoutParams)
    }
    
    fun setBlurImageView(coverUrl: String) {
        Glide.with(this)
            .load(coverUrl)
            .override(50)
            .transform(BlurTransformation(10))
            .dontAnimate()
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }
                
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    background = resource
                }
            })
    }
}