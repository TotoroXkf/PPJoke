package com.xkf.libcommon

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.xkf.libcommon.databinding.LayoutEmptyViewBinding

class EmptyView : FrameLayout {
    private val viewBinding =
        LayoutEmptyViewBinding.inflate(LayoutInflater.from(context), this, true)
    
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    
    fun setEmptyIcon(@DrawableRes iconRes: Int) {
        viewBinding.emptyIconView.setImageResource(iconRes)
    }
    
    fun setTitle(title: String) {
        if (!TextUtils.isEmpty(title)) {
            viewBinding.emptyTextView.visibility = View.VISIBLE
            viewBinding.emptyTextView.text = title
        } else {
            viewBinding.emptyTextView.visibility = View.GONE
        }
    }
    
    fun setButton(text: String, onClick: (() -> Unit)?) {
        if (!TextUtils.isEmpty(text)) {
            viewBinding.emptyActionView.visibility = View.VISIBLE
            viewBinding.emptyActionView.text = text
            viewBinding.emptyActionView.setOnClickListener {
                onClick?.invoke()
            }
        } else {
            viewBinding.emptyActionView.visibility = View.GONE
        }
    }
}