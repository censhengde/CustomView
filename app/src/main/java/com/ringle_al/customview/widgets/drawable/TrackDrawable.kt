package com.ringle_al.customview.widgets.drawable

import android.graphics.*
import android.util.Log

/**
 * create by 岑胜德
 * on 2019/11/26
 * 说明:
 *
 */
open class TrackDrawable() : BaseDrawable() {

    private val mFirstBgPaint = Paint()
    protected val mProgressPaint = Paint()

    private val mPaintWidth=60f

    private var mProgress = 0f

    init{



        mFirstBgPaint.color = Color.parseColor("#989595")
        mFirstBgPaint.alpha = 40
        mFirstBgPaint.strokeWidth = mPaintWidth
        mFirstBgPaint.isAntiAlias = true
        mFirstBgPaint.style = Paint.Style.STROKE

        //背景弧
        mPaint.color = Color.parseColor("#989595")
        mPaint.strokeWidth = (20).toFloat()
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.style = Paint.Style.STROKE

        mProgressPaint.color = Color.parseColor("#2189FF")
        mProgressPaint.strokeWidth = (20).toFloat()
        mProgressPaint.isAntiAlias = true
        mProgressPaint.strokeCap = Paint.Cap.ROUND
        mProgressPaint.style = Paint.Style.STROKE
    }

    /**
     *自定义轨道Drawable
     */
    companion object {
        const val TAG = "TrackDrawable"

    }

    override fun onLevelChange(level: Int): Boolean {
        mProgress = level.toFloat()
        return false//返回true会造成重绘
    }

    override fun draw(canvas: Canvas) {

        Log.e(TAG, "level=$level")
        drawFirstBg(canvas)
        drawSecondBg(canvas)
        drawProgress(canvas)


    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        //(我们的期望是外界给予的界限都能完整地包含自身的Drawable)
        mBounds.set(left+mPaintWidth,top+mPaintWidth,right+mPaintWidth,bottom+mPaintWidth)
    }

    protected fun drawArc(startAngle: Int, sweepAngle: Int, paint: Paint, canvas: Canvas?) {
        canvas?.save()
        canvas?.drawArc(mBounds, startAngle.toFloat(), sweepAngle.toFloat(), false, paint)
        canvas?.restore()
    }


    /**
     *画第一层背景
     */
    protected fun drawFirstBg(canvas: Canvas?) {
        canvas?.save()
        canvas?.drawArc(mBounds, -185f, 100f, false, mFirstBgPaint)
        canvas?.restore()

    }

    /**
     *画第二层背景
     */
    private fun drawSecondBg(canvas: Canvas?) {
        canvas?.save()
        canvas?.drawArc(mBounds, -180f, 90f, false, mPaint)
        canvas?.restore()

    }

    /**
     *画进度
     */
    protected fun drawProgress(canvas: Canvas?) {
        Log.e("drawProgress","mProgress=$mProgress")
        canvas?.save()
        canvas?.drawArc(mBounds, -180f, mProgress, false, mProgressPaint)
        canvas?.restore()

    }


}


