package pt.isec.theforgotten.rascunhos

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

const val TAG_AD = "AreaDesenho"

data class Linha(val path: Path, val cor: Int)

class AreaDesenho(context: Context) : View(context), GestureDetector.OnGestureListener {
    var corLinha: Int = Color.BLACK
        set(value) {
            field = value
            desenho.add(Linha(Path(), value))
        }

    var corFundo: Int = Color.WHITE
    constructor(context: Context, corFundo: Int) : this(context) {
        this.corFundo = corFundo
        setBackgroundColor(corFundo)
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 4.0f
        style = Paint.Style.STROKE
    }

    // private var path = Path()
    private val desenho : ArrayList<Linha> = arrayListOf(Linha(Path(), Color.BLACK))
    private val path : Path get() = desenho.last().path

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (linha in desenho) canvas?.drawPath(linha.path, paint.apply { color = linha.cor })
    }

    private val gestureDetector : GestureDetector by lazy { GestureDetector(context, this) }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (gestureDetector.onTouchEvent(event)) return true

        return super.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        Log.i(TAG_AD, "onDown: ${e!!.x} ${e.y}")
        path.moveTo(e.x, e.y)
        invalidate()
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
        Log.i(TAG_AD, "onShowPress: ${e!!.x} ${e.y}")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.i(TAG_AD, "onSingleTapUp: ${e!!.x} ${e.y}")
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float,
    ): Boolean {
        Log.i(TAG_AD, "onScroll: ${e2!!.x} ${e2.y}")
        path.lineTo(e2.x, e2.y)
        path.moveTo(e2.x, e2.y)
        invalidate()
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        Log.i(TAG_AD, "onLongPress: ${e!!.x} ${e.y}")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float,
    ): Boolean {
        Log.i(TAG_AD, "onFling: ${e2!!.x} ${e2.y}")
        return false
    }
}