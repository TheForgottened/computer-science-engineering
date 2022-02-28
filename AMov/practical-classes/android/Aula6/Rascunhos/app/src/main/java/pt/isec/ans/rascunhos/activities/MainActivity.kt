package pt.isec.ans.rascunhos.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import pt.isec.ans.rascunhos.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onFundoSolido(view: android.view.View) {
        val intent = Intent(this, FundoSolidoActivity::class.java)
        startActivity(intent)
    }

    fun onImagem(view: android.view.View) {
        val intent = Intent(this, FundoImagemActivity::class.java)
        intent.putExtra(FundoImagemActivity.TIPO_PARAM, FundoImagemActivity.ESCOLHER)
        startActivity(intent)
    }
    fun onFotografia(view: android.view.View) {
        val intent = Intent(this, FundoImagemActivity::class.java)
        intent.putExtra(FundoImagemActivity.TIPO_PARAM, FundoImagemActivity.CAPTURAR)
        startActivity(intent)
    }

    fun onHistorico(view: android.view.View) {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }
}