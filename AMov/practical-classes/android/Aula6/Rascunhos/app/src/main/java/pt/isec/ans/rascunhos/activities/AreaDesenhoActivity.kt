package pt.isec.ans.rascunhos.activities

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import pt.isec.ans.rascunhos.AreaDesenho
import pt.isec.ans.rascunhos.R
import pt.isec.ans.rascunhos.databinding.ActivityAreaDesenhoBinding
import pt.isec.ans.rascunhos.modelo.Rascunho

class AreaDesenhoActivity : AppCompatActivity() {
    companion object {
        const val TITULO_PARAM = "titulo"
        const val RED_PARAM = "red"
        const val GREEN_PARAM = "green"
        const val BLUE_PARAM = "blue"
        const val IMAGEM_PARAM = "imagem"
        const val INDEX_PARAM = "index"
    }

    // O rascunho pode ser: já existente, novo com cor de fundo, novo com imagem de fundo
    lateinit var rascunho: Rascunho
    lateinit var binding: ActivityAreaDesenhoBinding
    lateinit var areaDesenho: AreaDesenho

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAreaDesenhoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val titulo = intent.getStringExtra(TITULO_PARAM) ?: getString(R.string.str_no_name)

        if (intent.hasExtra(IMAGEM_PARAM)) {
            val imagePath = intent.getStringExtra(IMAGEM_PARAM)

            rascunho = Rascunho(titulo, imagemFundo = imagePath)
        } else {
            val r = intent.getIntExtra(RED_PARAM, 255)
            val g = intent.getIntExtra(GREEN_PARAM, 255)
            val b = intent.getIntExtra(BLUE_PARAM, 255)

            rascunho = Rascunho(titulo, corFundo = Color.rgb(r, g, b))
        }

        supportActionBar?.title = getString(R.string.rascunho) + ": " + titulo
        areaDesenho = AreaDesenho(this, rascunho)
        binding.frAreaDesenho.addView(areaDesenho)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_desenho, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.groupId) {
            R.id.grpCores -> {
                item.setChecked(true)
                areaDesenho.corLinha = Color.parseColor(item.title.toString())
                /*Mais seguro:
                areaDesenho.corLinha = when (item.itemId) {
                    R.id.mn -> Color.WHITE
                    R.id.mnAmarelo -> Color.YELLOW
                    R.id.mnAzul -> Color.rgb(0,0,128)
                    R.id.mnPreto -> Color.BLACK
                    else -> Color.BLACK
                }*/
            }
            else -> {
                when (item.itemId) {
                    R.id.mnGravar -> {
                        finish()
                    }
                    else -> return super.onOptionsItemSelected(item)
                }
            }
        }
        return true
    }
}