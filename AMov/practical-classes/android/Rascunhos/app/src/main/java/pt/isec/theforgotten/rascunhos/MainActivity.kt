package pt.isec.theforgotten.rascunhos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onFundoSolido(view: View) {
        // Toast.makeText(this, R.string.wip, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, FundoSolidoActivity::class.java)
        startActivity(intent)
    }

    fun onImagem(view: View) {
        val intent = Intent(this, FundoImagemActivity::class.java)
        intent.putExtra(FundoImagemActivity.TIPO_FONTE, FundoImagemActivity.ESCOLHER)
        startActivity(intent)
    }

    fun onFotografia(view: View) {
        val intent = Intent(this, FundoImagemActivity::class.java)
        intent.putExtra(FundoImagemActivity.TIPO_FONTE, FundoImagemActivity.CAPTURAR)
        startActivity(intent)
    }

    fun onHistorico(view: View) {
        Snackbar.make(view, "WIP", Snackbar.LENGTH_SHORT).show()
    }
}