package pt.isec.ans.rockpaperscissors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //to do: should verify if the network is available

        findViewById<Button>(R.id.btnServer).setOnClickListener {
            startGame(SERVER_MODE)
        }
        findViewById<Button>(R.id.btnClient).setOnClickListener {
            startGame(CLIENT_MODE)
        }
    }

    fun startGame(mode : Int) {
        val intent = Intent(this,GameActivity::class.java).apply {
            putExtra("mode",mode)
        }
        startActivity(intent)
    }
}