package pt.isec.theforgotten.rascunhos

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pt.isec.theforgotten.rascunhos.databinding.ActivityFundoImagemBinding

class FundoImagemActivity : AppCompatActivity() {
    companion object {
        val ESCOLHER = 1
        val CAPTURAR = 2
        val TIPO_FONTE = "tipo"
    }

    private lateinit var viewBinding : ActivityFundoImagemBinding
    private var modo : Int = ESCOLHER
    private var permissionsGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityFundoImagemBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        modo = intent.getIntExtra(TIPO_FONTE, ESCOLHER)

        if (modo == ESCOLHER) {
            viewBinding.buttonImagem.text = "Escolher Imagem"
        } else {
            viewBinding.buttonImagem.text = "Nova Fotografia"
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1234)
        } else {
            permissionsGranted = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1234) {
            permissionsGranted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}