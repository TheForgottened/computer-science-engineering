package pt.isec.ans.rascunhos.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import com.google.android.material.snackbar.Snackbar
import pt.isec.ans.rascunhos.R
import pt.isec.ans.rascunhos.databinding.ActivityFundoSolidoBinding

class FundoSolidoActivity : AppCompatActivity() {
    lateinit var b : ActivityFundoSolidoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFundoSolidoBinding.inflate(layoutInflater)
        setContentView(b.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        b.seekRed.apply {
            max = 255
            progress = 255
            setOnSeekBarChangeListener(procSeekBar)
        }
        b.seekGreen.apply {
            max = 255
            progress = 255
            setOnSeekBarChangeListener(procSeekBar)
        }
        b.seekBlue.apply {
            max = 255
            progress = 255
            setOnSeekBarChangeListener(procSeekBar)
        }
        updatePreview()
    }

    val procSeekBar = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            updatePreview()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

    fun updatePreview() {
        val cor = Color.rgb(b.seekRed.progress,b.seekGreen.progress,b.seekBlue.progress)
        b.frPreview.setBackgroundColor(cor)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_criar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mnCriar) {
            if (b.edTitulo.text.trim().isEmpty()) {
                Snackbar.make(b.edTitulo,
                    R.string.msg_titulo_vazio,
                    Snackbar.LENGTH_LONG).show()
                b.edTitulo.requestFocus()
                return true;
            }
            val intent = Intent(this, AreaDesenhoActivity::class.java)
            intent.putExtra(AreaDesenhoActivity.TITULO_PARAM,b.edTitulo.text.toString())
            intent.putExtra(AreaDesenhoActivity.RED_PARAM,b.seekRed.progress)
            intent.putExtra(AreaDesenhoActivity.GREEN_PARAM,b.seekGreen.progress)
            intent.putExtra(AreaDesenhoActivity.BLUE_PARAM,b.seekBlue.progress)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}