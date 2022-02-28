package pt.isec.theforgotten.rascunhos

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import pt.isec.theforgotten.rascunhos.databinding.ActivityFundoSolidoBinding

class FundoSolidoActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityFundoSolidoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fundo_solido)

        viewBinding = ActivityFundoSolidoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewBinding.seekbarRed.apply {
            max = 255
            progress = 255
            setOnSeekBarChangeListener(procSeekBar)
        }

        viewBinding.seekbarBlue.apply {
            max = 255
            progress = 255
            setOnSeekBarChangeListener(procSeekBar)
        }

        viewBinding.seekbarGreen.apply {
            max = 255
            progress = 255
            setOnSeekBarChangeListener(procSeekBar)
        }

        updateColorPreview()
    }
    
    private val procSeekBar = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            updateColorPreview()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) { /* NOT USED */ }

        override fun onStopTrackingTouch(seekBar: SeekBar?) { /* NOT USED */ }
    }

    private fun updateColorPreview() {
        viewBinding.colorSquare.setBackgroundColor(Color.rgb(
            viewBinding.seekbarRed.progress,
            viewBinding.seekbarGreen.progress,
            viewBinding.seekbarBlue.progress
        ))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_criar, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != R.id.mnCriar) return super.onOptionsItemSelected(item)

        val intent = Intent(this, AreaDesenhoActivity::class.java)
        intent.putExtra("titulo", viewBinding.editTitulo.text.toString())
        intent.putExtra("red", viewBinding.seekbarRed.progress)
        intent.putExtra("green", viewBinding.seekbarGreen.progress)
        intent.putExtra("blue", viewBinding.seekbarBlue.progress)
        startActivity(intent)
        finish()
        return true
    }
}