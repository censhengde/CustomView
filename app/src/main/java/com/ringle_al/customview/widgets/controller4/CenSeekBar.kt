package com.ringle_al.customview.widgets.controller4

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * create by 岑胜德
 * on 2019/11/27
 * 说明:每一个CenSeekBar由Track与Thumb两部分构成
 *
 */
abstract class CenSeekBar(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    companion object {
        /**
         *获取两点间距离
         */
        fun getDistance(from: PointF, to: PointF) =
            sqrt((from.x - to.x).pow(2) + (from.y - to.y).pow(2))

        fun getDistance(x1: Float, y1: Float, x2: Float, y2: Float) =
            sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))

    }

    private var isThumbPressed: Boolean = false//Thumb是否被摁下
      var mMin = 0
      var mMax = 100

    //轨道条
    protected val mTrack by lazy { setTrack() }
    //Thumb
    protected val mThumb by lazy { setThumb() }

    //让子类设置Track
    abstract fun setTrack(): Track
    //让子类设置Thumb
    abstract fun setThumb(): Thumb
    //屏幕密度
    private val dpDensity: Float = context?.resources?.displayMetrics?.density ?: 0f
    //回调接口
    private lateinit var mChangeListener: OnChangeListener

     fun setListener(listener: OnChangeListener) {
        mChangeListener = listener
    }

    /**
     *设置进度增量
     */
     fun incrementProgressBy(diff: Int) {
        mTrack.progress += diff
        checkTrack(mTrack)
        //TODO("这里需要一个算法,让Track与Thumb建立起联系,让子类去决定")
        holdThumbPosition(mTrack, mThumb)

        //回调进度
        val percent = mTrack.progress * 100 / mMax
        mChangeListener.onProgressChanged(percent)
        invalidate()//重新绘制
    }

    /**
     *未来的进度条可能是横的,竖的,弯的,斜的......
     * 所以Track与Thumb关联的算法各异,让子类去决定
     * 这里先来个默认实现
     */
    abstract fun holdThumbPosition(track: Track, thumb: Thumb)

    /**
     *测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //这里本应该统一测量,
        stuMeasure()


    }

    protected open fun stuMeasure() {
        val left = left + dp2Px(15f)
        val top = top + dp2Px(15f)
        val right = left + width + 220//矩形宽
        val bottom = top + width + 220//矩形宽

        mTrack.setBounds(
            left.roundToInt(),
            top.roundToInt(),
            right.roundToInt(),
            bottom.roundToInt()
        )

        mThumb.x = (mTrack.getBounds().left).toFloat()

        mThumb.y = (mTrack.getBounds().top + mTrack.getBounds().height() / 2).toFloat()
    }

    /**
     *尺寸转换
     */

    fun dp2Px(dp: Float) = dp * dpDensity + 0.5f

    fun px2Dp(px: Float) = px / dpDensity + 0.5f

    /**
     *绘制
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTrack(canvas)
        drawThumb(canvas)
    }

    @SuppressLint("Range")
    private fun drawTrack(canvas: Canvas?) {
        val t = mTrack
        canvas?.save()
        canvas?.let { t.drawSelf(it) }
        canvas?.restore()


    }

    /**
     *画Thumb
     */
    @SuppressLint("Range")
    private fun drawThumb(canvas: Canvas?) {
        val thumb = mThumb
        canvas?.save()
        canvas?.let { thumb.drawSelf(it) }
        canvas?.restore()
    }


    /**
     *事件处理
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isNearByArc(event.x, event.y)) {
                    //移动Thumb
                    isThumbPressed = true
                    mChangeListener.onStartTrackingTouch()
                    move(event.x, event.y)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isNearByArc(event.x, event.y)) {
                    //
                    move(event.x, event.y)

                }
            }
            MotionEvent.ACTION_UP -> {
                if (isThumbPressed) {
                    isThumbPressed = false
                    //记录拖拽后坐标
                    setThumbPosition()
                }
            }
        }
        return true
    }

    /**
     *移动Thumb
     */
    private fun move(x: Float, y: Float) {
        //todo 这里需要一个算法,让触摸点与Track和Thumb建立起联系
        val track = mTrack
        val thumb = mThumb
        holdOnDrag(x, y, track, thumb)
        //TODO("将进度回调出去")
        val percent = (track.progress * 100 / mMax)
        mChangeListener.onProgressChanged(percent)
        invalidate()

    }

    /**
     * Track与Thumb关联的算法各异,让子类去重写
     *tX,tY:触摸点坐标
     */
    abstract fun holdOnDrag(
        tX: Float,
        tY: Float,
        track: Track,
        thumb: Thumb
    )

        //约束
    fun checkTrack(track: Track) {
        if (track.progress > mMax) track.progress = mMax
        if (track.progress < mMin) track.progress = mMin
    }

    fun checkThumb(
        x: Float,
        y: Float,
        minX: Float,
        maxX: Float,
        minY: Float,
        maxY: Float
    ): Boolean {

        return x in minX..maxX && y in minY..maxY
    }

    /**
     *判断触摸位置是否在弧形轨道附近
     */
    private fun isNearByArc(x: Float, y: Float): Boolean {
        //todo 这里需要一个算法,让触摸点与Thumb建立起联系

        val r = mTrack.getBounds().width() / 2
        //拿到圆心坐标
        val oX = (mTrack.getBounds().left + mTrack.getBounds().width() / 2).toFloat()
        val oY = (mTrack.getBounds().top + mTrack.getBounds().height() / 2).toFloat()
        //拿到触摸点到圆心距离
        val d = getDistance(x, y, oX, oY)
        val min = r - (40f)
        val max = r + (40f)
        Log.e("isNearByArc", "r=$r")
        return d in min..max
    }

    /**
     *设置Thumb位置
     */
    private fun setThumbPosition() {
        //
        val percent = (mTrack.progress * 100 / mMax)
        mChangeListener.onStopTrackingTouch(percent)

    }


    /**
     *进度条轨道封装,日后轨道条各种花里胡哨内容在此扩展
     */
    open class Track {
        //进度值
        var progress = 0
        //轨道区域
        private lateinit var mBounds: Rect
        //轨道样式
        private lateinit var mTrackBar: Drawable

        fun getBounds(): Rect = mBounds
        fun setBounds(rect: Rect) {
            mBounds = rect
        }

        fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
            mBounds = Rect(left, top, right, bottom)
            mTrackBar.bounds = (mBounds)
        }


        fun setDrawable(dr: Drawable) {
            this.mTrackBar = dr
        }

        //画自己
        internal fun drawSelf(canvas: Canvas) {
            this.mTrackBar.level = progress//根据progress调控Drawable状态
            this.mTrackBar.draw(canvas)
        }

    }

    /**
     *Thumb上下文信息封装,目前仅有位置坐标,日后Thumb的各种花里胡哨内容在这里扩展
     */
    open class Thumb(val width: Int, val height: Int) : PointF() {

        private lateinit var thumbDrawable: Drawable

        internal fun setDrawable(dr: Drawable) {
            thumbDrawable = dr
        }

        internal fun getDrawable() = thumbDrawable

        /**
         *画自己
         */
        internal fun drawSelf(canvas: Canvas) {
            Log.e("drawSelf", "x=$x,y=$y")
            val left = x.roundToInt()
            val top = y.roundToInt()
            val right = left + width
            val bottom = top + height
            thumbDrawable.setBounds(left, top, right, bottom)
            thumbDrawable.draw(canvas)
        }


    }

    /**
     *进度回调接口
     *
     */
    interface OnChangeListener {

        fun onStartTrackingTouch()
        /**
         *进度回调,避免重量级操作
         */
        fun onProgressChanged(progress: Int)


        fun onStopTrackingTouch(progress: Int)
    }

}