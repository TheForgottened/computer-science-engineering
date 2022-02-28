package pt.isec.amov.ajp.reversi.game

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class GameViewModel : ViewModel() {
    private lateinit var db : FirebaseFirestore

    val gameModel = GameModel

    fun makeMove(position: Int) {
        gameModel.makeMove(position)
    }

    fun getAllPossibleMoves(currentPlayer: Int): List<Int> {
        return gameModel.getAllPossibleMoves(currentPlayer)
    }

    
}