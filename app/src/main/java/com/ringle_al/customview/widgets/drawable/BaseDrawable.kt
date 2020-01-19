package com.ringle_al.customview.widgets.drawable

import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable

/**
 * create by 岑胜德
 * on 2019/12/2
 * 说明:
 *
 */
abstract class BaseDrawable(): Drawable() {
    protected val mPaint by lazy { Paint() }

    protected val mBounds = RectF()//当前Drawable边界

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }
    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        mBounds.set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }
}