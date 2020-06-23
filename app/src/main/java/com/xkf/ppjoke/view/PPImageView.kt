package com.xkf.ppjoke.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.xkf.libcommon.Utils
import jp.wasabeef.glide.transformations.BlurTransformation


class PPImageView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

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
        val layoutParam = layoutParams as ViewGroup.MarginLayoutParams
        layoutParam.width = finalWidth
        layoutParam.height = finalHeight
        layoutParams = layoutParam
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