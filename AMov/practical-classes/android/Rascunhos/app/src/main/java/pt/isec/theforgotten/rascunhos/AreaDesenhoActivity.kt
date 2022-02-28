package pt.isec.theforgotten.rascunhos

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout

class AreaDesenhoActivity : AppCompatActivity() {
    private lateinit var titulo : String
    private lateinit var frameLayout: FrameLayout
    private lateinit var areaDesenho: AreaDesenho

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_desenho)

        titulo = intent.getStringExtra("titulo") ?: "Sem Nome"
        supportActionBar?.title = "Rascunho: " + titulo

        val r = intent.getIntExtra("red", 255)
        val g = intent.getIntExtra("green", 255)
        val b = intent.getIntExtra("blue", 255)

        frameLayout = findViewById<FrameLayout>(R.id.frameAreaDesenho)
        frameLayout.setBackgroundColor(Color.rgb(r, g, b))

        areaDesenho = AreaDesenho(this, Color.rgb(r, g, b))
        frameLayout.addView(areaDesenho)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_desenho, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.groupId) {
            R.id.grupo_cores -> {
                areaDesenho.corLinha = Color.parseColor(item.title.toString())
                item.setChecked(true)
            }
            else -> {
                when (item.itemId) {
                    R.id.menu_gravar -> finish()
                    else -> return super.onOptionsItemSelected(item)
                }
            }
        }

        return true
    }
}