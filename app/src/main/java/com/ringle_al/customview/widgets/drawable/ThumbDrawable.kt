package com.ringle_al.customview.widgets.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.graphics.transform
import com.ringle_al.customview.R

/**
 * create by 岑胜德
 * on 2019/11/26
 * 说明:
 *
 */
const val TAG = "ThumbDrawable"

open class ThumbDrawable(val context: Context) : BaseDrawable() {
    protected val mBmpRectF = RectF()
    protected val mThumbBmp by lazy { setThumb() }

    protected open fun setThumb(): Bitmap {
        return BitmapFactory.decodeResource(context.resources, R.drawable.btn_thumb_normal)
    }


    override fun draw(canvas: Canvas) {
        Log.e(TAG, "x=${mBounds.left},y=${mBounds.top}")
        canvas.drawBitmap(mThumbBmp, null, mBmpRectF, mPaint)

    }



    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT//表示当前Drawbale具有Alpha通道
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    /**
     *由外部决定边界
     */
    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        Log.e("ThumbDrawable", "Bounds:${mBounds.left},${mBounds.top}")
        //计算bitmap位置(以mBounds.left,mBounds.top为中心)
        val l = mBounds.left - mBounds.width() / 2
        val t = mBounds.top - mBounds.width() / 2
        val r = l + mBounds.width()
        val b = t + mBounds.height()
        mBmpRectF.set(l, t, r, b)
    }


}