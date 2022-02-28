package pt.isec.ans.rascunhos.modelo

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Path

data class Rascunho(
    var titulo: String? = null,
    val linhas: ArrayList<Linha> = arrayListOf(Linha(Path(),Color.BLACK)),
    val corFundo: Int = Color.WHITE,
    val imagemFundo: String? = null, // Imagem da galeria ou fotografia capturada
    var preview: Bitmap? = null // Será usado para o histórico
) {
    fun addLinha(corLinha: Int = Color.BLACK) {
        linhas.add(Linha(Path(),corLinha))
    }
}
