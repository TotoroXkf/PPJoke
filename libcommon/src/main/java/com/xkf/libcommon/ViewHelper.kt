package com.xkf.libcommon

import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider

const val RADIUS_ALL = 0
const val RADIUS_LEFT = 1
const val RADIUS_TOP = 2
const val RADIUS_RIGHT = 3
const val RADIUS_BOTTOM = 4

fun setViewOutline(view: View, attributes: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
    val array = view.context.obtainStyledAttributes(
        attributes, R.styleable.viewOutLineStrategy, defStyleAttr, defStyleRes
    )
    val radius = array.getDimensionPixelSize(R.styleable.viewOutLineStrategy_radius, 0)
    val radiusSide = array.getInt(R.styleable.viewOutLineStrategy_radiusSide, 0)
    array.recycle()

    setViewOutline(view, radius, radiusSide)
}

fun setViewOutline(view: View, radius: Int, radiusSide: Int) {
    if (radius <= 0) {
        return
    }

    view.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            val width = view.width
            val height = view.height
            if (width <= 0 || height <= 0) {
                return
            }

            if (radiusSide != RADIUS_ALL) {
                var left = 0
                var top = 0
                var right = 0
                var bottom = 0
                when (radiusSide) {
                    RADIUS_LEFT -> {
                        right += radius
                    }
                    RADIUS_TOP -> {
                        bottom += radius
                    }
                    RADIUS_RIGHT -> {
                        left -= radius
                    }
                    RADIUS_BOTTOM -> {
                        top -= radius
                    }
                }
                outline.setRoundRect(left, top, right, bottom, radius.toFloat())
            } else {
                if (radius > 0) {
                    outline.setRoundRect(0, 0, width, height, radius.toFloat())
                } else {
                    outline.setRect(0, 0, width, height)
                }
            }
        }
    }

//    view.clipToOutline = radius > 0;
//    view.invalidate()
}
