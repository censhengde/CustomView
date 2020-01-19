package com.ringle_al.customview.widgets.drawable

import android.content.Context
import android.graphics.*
import com.ringle_al.customview.R

/**
 * create by 岑胜德
 * on 2019/12/2
 * 说明:
 *
 */
class VolTrackDrawable(val context: Context) : TrackDrawable() {
    private val mBgBmp by lazy(LazyThreadSafetyMode.NONE) {
        BitmapFactory.decodeResource(context.resources, R.drawable.ic_vol_indi_bar_right)
    }
    private val mBgPaint = Paint()
    private val mBgRectF=RectF()

    init {
        mProgressPaint.color = Color.parseColor("#008000")
        mProgressPaint.strokeWidth = (60).toFloat()
        mProgressPaint.alpha=160
        mProgressPaint.strokeCap=Paint.Cap.BUTT
        mProgressPaint.isAntiAlias = true
        mProgressPaint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas) {
        drawBg(canvas)
        super.drawFirstBg(canvas)
        super.drawProgress(canvas)

    }

    private fun drawBg(canvas: Canvas) {
        canvas.save()
        canvas.drawBitmap(mBgBmp, null, mBgRectF, mBgPaint)
        canvas.restore()
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        val l=mBounds.left
        val t=mBounds.top-10
        val r=l+mBounds.width()/2
        val b=t+mBounds.height()/2
        mBgRectF.set(l,t,r,b)
    }

}