package com.ringle_al.customview.widgets.controller4

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ringle_al.customview.R
import com.ringle_al.customview.widgets.drawable.*
import kotlinx.android.synthetic.main.view_play_controller4.view.*
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * create by 岑胜德
 * on 2019/11/27
 * 说明:
 *
 */
@Suppress("PLUGIN_WARNING")
class PlayController4(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var isStoped: Boolean = false
    private val ringSeekBar by lazy { ring_seek_bar as ArcSeekBar }

    private val ringVolumeBar by lazy { ring_volume_bar as VolumeBar }

    //进度增量
    private var mDiff: Int = 1

    fun setDiff(diff: Int) {
        this.mDiff = diff
    }

    init {
        //注入布局
        setWillNotDraw(true)
        LayoutInflater.from(context).inflate(R.layout.view_play_controller4, this, true)

        //快进
        btn_forward.setOnClickListener {
            ringSeekBar.incrementProgressBy(mDiff)
        }
        //快退
        btn_back.setOnClickListener {
            ringSeekBar.incrementProgressBy(-mDiff)
        }
        //暂停
        btn_pause.setOnClickListener {
            if (!isStoped) {
                isStoped = true
                btn_pause.setBackgroundResource(R.drawable.btn_play_unable)

            } else {
                isStoped = false
                btn_pause.setBackgroundResource(R.drawable.btn_pause_normal)
            }
        }

        //音量控制
        ringVolumeBar.setListener(object : CenSeekBar.OnChangeListener {
            override fun onStartTrackingTouch() {
            }

            override fun onProgressChanged(progress: Int) {
                when (progress) {
                    0 -> iv_volume.setImageResource(R.drawable.ic_volume_mute)//静音
                    in 1..22 -> iv_volume.setImageResource(R.drawable.ic_volume_lower)
                    in 23..44 -> iv_volume.setImageResource(R.drawable.ic_volume_low)
                    in 45..66 -> iv_volume.setImageResource(R.drawable.ic_volume_high)
                    in 67..90 -> iv_volume.setImageResource(R.drawable.ic_volume_higher)
                }
            }

            override fun onStopTrackingTouch(progress: Int) {
            }

        })
    }

    /**
     *设置回调接口
     */
    fun setOnChangeListener(listener: CenSeekBar.OnChangeListener) {
        ringSeekBar.setListener(listener)
    }



    /**
     *弧形进度条
     */
    private open class ArcSeekBar(context: Context?, attrs: AttributeSet?) : CenSeekBar(context, attrs) {

        init {
            mMin=0
            mMax=90
        }
        override fun setTrack(): Track {
            //这里应当由外部定义,先写死看效果
            val bgDr = TrackDrawable()
            val track = Track()
            track.setDrawable(bgDr)
            return track
        }

        override fun setThumb(): Thumb {
            val thumb = Thumb(dp2Px(15f).roundToInt(), dp2Px(15f).roundToInt())
            thumb.setDrawable(ThumbDrawable(context))
            return thumb
        }


        /**
         *当点击快进/快退时根据Track计算出Thumb位置
         */
        override fun holdThumbPosition(
            track: Track,
            thumb: Thumb
        ) {
            //拿到圆弧半径
            val r =track.getBounds().width().toFloat() / 2
            //拿到圆心角
            val angle = Math.toRadians(track.progress.toDouble()).toFloat()
            val tempX = (track.getBounds().left + r) - cos(angle) * r
            val tempY = (track.getBounds().top + r) - sin(angle) * r

            val minX = track.getBounds().left.toFloat()
            val maxX = minX + r
            val minY = track.getBounds().top.toFloat()
            val maxY = minY + r
            //约束
            if (checkThumb(tempX, tempY, minX, maxX, minY, maxY)) {
                thumb.x = tempX
                thumb.y = tempY
            }
        }

        /**
         *当滑动时根据触摸点计算出Track与Thumb位置
         */
        override fun holdOnDrag(
            tX: Float,
            tY: Float,
            track: Track,
            thumb: Thumb
        ) {
            val r = track.getBounds().width() / 2
            //拿到圆心坐标
            val oX = (track.getBounds().left).toFloat() + r
            val oY = (track.getBounds().top).toFloat() + r
            //拿到触摸点到圆心距离
            val d = getDistance(tX, tY, oX, oY)
            //todo 这里考虑一个问题,当tX>oX时,三角函数运算得到的是补角
            if (tX !in track.getBounds().left..oX.toInt()) return
            //拿到弧度 val arc_angle = asin((mArcRectF_C.y - y) / r)
            val arcAngle = asin((oY - tY) / d)
            //拿到圆心角
            val oAngle = (arcAngle * 180 / Math.PI).toInt()
            track.progress = oAngle
            //约束
            checkTrack(track)

            val tempX = oX - (oX - tX) * r / d
            val tempY = oY - (oY - tY) * r / d
            //约束thumb位置
            if (checkThumb(tempX, tempY, (oX - r), oX, (oY - r), oY)) {
                thumb.x = tempX
                thumb.y = tempY
            }

        }


        override fun stuMeasure() {
            val left = left + 15f
            val top = top + 15f
            val right = left + width + dp2Px(270f)//矩形宽
            val bottom = top + width + dp2Px(270f)//矩形宽

            mTrack.setBounds(
                left.roundToInt(),
                top.roundToInt(),
                right.roundToInt(),
                bottom.roundToInt()
            )

            mThumb.x = (mTrack.getBounds().left).toFloat()

            mThumb.y = (mTrack.getBounds().top + mTrack.getBounds().height() / 2).toFloat()
        }
    }

    /**
     *音量条
     */
    private class VolumeBar(context: Context?, attrs: AttributeSet?) : ArcSeekBar(context, attrs) {

        init {
            mMin=0
            mMax=90
        }

        override fun setTrack(): Track {
            //这里应当由外部定义,先写死看效果
            val bgDr = VolTrackDrawable(context)
            val track = Track()
            track.setDrawable(bgDr)
            return track
        }

        override fun setThumb(): Thumb {

            val thumb = Thumb(50, 50)
            thumb.setDrawable(VolThumbDrawable(context))
            return thumb
        }


        override fun stuMeasure() {
            val left = left +95f
            val top = top + 95f
            val right = left + width + 350//矩形宽
            val bottom = top + width + 350//矩形宽

            mTrack.setBounds(
                left.roundToInt(),
                top.roundToInt(),
                right.roundToInt(),
                bottom.roundToInt()
            )

            mThumb.x = (mTrack.getBounds().left).toFloat()

            mThumb.y = (mTrack.getBounds().top + mTrack.getBounds().height() / 2).toFloat()

            (mThumb.getDrawable() as VolThumbDrawable).oX =
                (mTrack.getBounds().left + mTrack.getBounds().width() / 2).toFloat()
            (mThumb.getDrawable() as VolThumbDrawable).oY =
                (mTrack.getBounds().top + mTrack.getBounds().height() / 2).toFloat()

        }

    }
}