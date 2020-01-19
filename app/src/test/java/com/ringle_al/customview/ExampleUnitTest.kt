package com.ringle_al.customview

import android.graphics.PointF
import org.junit.Test

import org.junit.Assert.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
        val p1=PointF(3.toFloat(),0f)
        val p2=PointF(0.toFloat(),4f)
    @Test
    fun addition_isCorrect() {
     val s= getDistance(p1,p2)
//        val a=(3-0).toFloat().pow(2)

        print("距离:${s}")
    }



}


fun getDistance(from:PointF,to:PointF):Float{
return sqrt((from.x - to.x).pow(2) + (from.y - to.y).pow(2))
}

//fun getDistance(from:PointF,to:PointF)= sqrt(25.toFloat())