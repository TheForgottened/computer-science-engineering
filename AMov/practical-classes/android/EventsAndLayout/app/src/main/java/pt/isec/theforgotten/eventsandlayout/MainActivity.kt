package pt.isec.theforgotten.eventsandlayout

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import pt.isec.theforgotten.eventsandlayout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var clickCounter : Int = 0

    private lateinit var tvMsg : TextView
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        tvMsg = findViewById(R.id.tvMsg)
        tvMsg.text = "AMOV, DEIS, 2021/22"

        binding.tvMsgWorld.text = "cringe"

        binding.buttonOne.setOnClickListener(this)
        binding.buttonTwo.setOnClickListener(ProcButtonTwo(binding.tvMsgWorld))
    }

    override fun onClick(v: View?) {
        binding.tvMsgWorld.text = when (clickCounter++ % 2) {
            0 -> "stoopid"
            1 -> "i cont belif u'v done dis"
            else -> "unexpected"
        }
    }

    class ProcButtonTwo(private val tv: TextView) : View.OnClickListener {
        private var clickCounter : Int = 0

        override fun onClick(v: View?) {
            tv.text = when (clickCounter++ % 2) {
                0 -> "não"
                1 -> "morre diabo"
                else -> "unexpected"
            }
        }

    }
}
