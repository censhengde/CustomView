package com.ringle_al.customview.widgets.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log

/**
 * create by 岑胜德
 * on 2019/11/26
 * 说明:
 *
 */
@Deprecated("暂时用不上")
class ProgressDrawble : TrackDrawable() {
    companion object {

        const val TAG: String = "ProgressDrawble"
    }

    init {
        //进度弧

        mPaint.color = Color.parseColor("#2189FF")
        mPaint.strokeWidth = (20).toFloat()
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        Log.e(TAG, "level=$level")
        checkLevel()
        super.drawArc(-180, level, mPaint, canvas)
    }

    /**
     *校验level
     */
    private fun checkLevel() {
        if (level > 90) {
            level = 90
        }
        if (level < 0) {
            level = 0
        }

        Log.e("checkLevel", "level:$level")
    }
}