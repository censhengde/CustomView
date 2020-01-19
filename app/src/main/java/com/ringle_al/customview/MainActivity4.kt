package com.ringle_al.customview

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ringle_al.customview.widgets.controller4.CenSeekBar
import kotlinx.android.synthetic.main.activity_main4.*

class MainActivity4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        play_ccontroller.setOnChangeListener(object : CenSeekBar.OnChangeListener {
            override fun onStartTrackingTouch() {

            }

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(progress: Int) {
                tv_progress.text = "进度:$progress%"
            }

            override fun onStopTrackingTouch(progress: Int) {

            }

        })

    }
}
