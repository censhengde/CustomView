package com.ringle_al.customview.widgets.drawable

import android.content.Context
import android.graphics.*
import android.util.Log
import com.ringle_al.customview.R
import kotlin.math.atan

/**
 * create by 岑胜德
 * on 2019/12/3
 * 说明:
 *
 */
class VolThumbDrawable(context: Context) : ThumbDrawable(context) {
    //圆心坐标
    var oX = 0f
    var oY = 0f
    //圆心角
    private var mDegrees = 0f
    override fun setThumb(): Bitmap {
        return BitmapFactory.decodeResource(context.resources, R.drawable.ic_vol_arrow_normal)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        //根据Thumb位置计算出中心点与圆弧中心点连线所成角度
        val x = mBounds.left
        val y = mBounds.top
        //拿到圆心角
        mDegrees = (atan((oY - y) / (oX - x)) * 180 / Math.PI).toFloat()
        Log.e("VolThumbDrawable", "oY=${oY},oX=${oX},degrees=$mDegrees")

    }


    override fun draw(canvas: Canvas) {
        canvas.rotate(mDegrees,mBounds.left,mBounds.top)
        super.draw(canvas)
//        canvas.drawBitmap(mThumbBmp, mMatrix, mPaint)
    }
}