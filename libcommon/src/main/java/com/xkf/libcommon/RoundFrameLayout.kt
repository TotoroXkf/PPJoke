package com.xkf.libcommon

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout


class RoundFrameLayout : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context, attrs, defStyleAttr, 0
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        setBackgroundColor(Color.WHITE)
        setViewOutline(this, attrs, defStyleAttr, defStyleRes)
    }

    fun setRoundRect(radius: Int, radiusSide: Int) {
        setViewOutline(this, radius, radiusSide)
    }
}