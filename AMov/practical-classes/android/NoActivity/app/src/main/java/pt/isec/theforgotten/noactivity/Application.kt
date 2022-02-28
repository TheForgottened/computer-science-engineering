package pt.isec.theforgotten.noactivity

import android.util.Log

class Application : android.app.Application() {
    private var _valor = 0

    val valor: Int
        get() { return _valor++ }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Application.onCreate")
    }
}