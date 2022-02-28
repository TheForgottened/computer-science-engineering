package pt.isec.theforgotten.noactivity

import android.app.Activity
import android.os.Bundle
import android.util.Log

const val TAG = "Main"

class Main : Activity() {
    val app : Application by lazy { application as Application }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        Log.i(TAG, "onCreate" + app.valor)
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart" + app.valor)
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart" + app.valor)
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop" + app.valor)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy" + app.valor)
    }
}