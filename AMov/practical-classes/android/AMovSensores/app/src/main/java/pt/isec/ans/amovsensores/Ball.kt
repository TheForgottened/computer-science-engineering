package pt.isec.ans.amovsensores

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.DITHER_FLAG

class Ball(x : Float, y : Float, var color : Int = Color.BLUE) :
    Sprite(x,y,2*BALL_RADIUS,2*BALL_RADIUS,0f,0f,false) {

    companion object {
        private const val BALL_RADIUS = 20f
    }
    var paint = Paint(DITHER_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        color = this@Ball.color
        strokeWidth = 2f
    }

    init {
        wallForce = 0.7f
    }

    override fun onDraw(c: Canvas?) {
        if (c==null) return
        c.drawCircle(x, y, BALL_RADIUS, paint)
    }
}