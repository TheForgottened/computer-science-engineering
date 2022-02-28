package pt.isec.ans.amovsensores

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.AttributeSet
import android.view.Surface
import android.view.View
import kotlin.random.Random

class Board(context: Context, attrs: AttributeSet) :
    View(context,attrs), SensorEventListener {
    companion object {
        private const val SPEEDFACTOR = 5f
    }

    var lstSprites: ArrayList<Sprite> = ArrayList()

    init {
        var colorBall = Color.BLACK
        var nrBalls = 1
        context?.theme?.obtainStyledAttributes(attrs,R.styleable.Board,0,0)?.apply {
            nrBalls = getInt(R.styleable.Board_nrBalls,1)
            colorBall = getInt(R.styleable.Board_colorBall,Color.MAGENTA)
        }
        repeat(nrBalls) {
            lstSprites.add(Ball(Random.nextFloat()*400, Random.nextFloat() * 200, colorBall))
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (s in lstSprites) { s.onDraw(canvas) }
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        for (s in lstSprites) {
            s.setLimits(0f, w.toFloat(), 0f, h.toFloat())
        }
    }
    fun applyAcceleration(accX: Float, accY: Float,
                          elapsedTime: Float) {
        for (s in lstSprites) {
            s.applyAcceleration(accX, accY, elapsedTime)
        }
        invalidate()
    }

    var lasttime: Long = -1

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER && display != null) {
            if (lasttime > 0) {
                var sx = 0f
                var sy = 0f
                when (display!!.rotation) {
                    Surface.ROTATION_0   -> { sx = -event.values[0]; sy = event.values[1]  }
                    Surface.ROTATION_90  -> { sx = event.values[1];  sy = event.values[0]  }
                    Surface.ROTATION_180 -> { sx = event.values[0];  sy = -event.values[1] }
                    Surface.ROTATION_270 -> { sx = -event.values[1]; sy = -event.values[0] }
                }
                val deltatime = SPEEDFACTOR * (event.timestamp - lasttime) / 1000000000.0f
                applyAcceleration(sx, sy, deltatime)
            }
            lasttime = event.timestamp
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}