package pt.isec.amov.ajp.reversi.game

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import org.json.JSONObject
import pt.isec.amov.ajp.reversi.data.GameState
import pt.isec.amov.ajp.reversi.data.JSONHelper
import pt.isec.amov.ajp.reversi.data.Player
import java.io.PrintStream
import kotlin.concurrent.thread

class NetworkGameViewModel : ViewModel() {
    private lateinit var db : FirebaseFirestore
    var isClient : Boolean = false
    lateinit var recyclerViewAdapter : NetworkRecyclerViewAdapter

    val gameModel = GameModel

    var myPlayerNumber = -1

    fun prepareThreads() {
        if (isClient) {
            thread {
                gameModel.serverSocket!!.getInputStream()?.run {
                    while (gameModel.gameState.value != GameState.FINISHED) {
                        try {
                            val bufferedReader = this.bufferedReader()

                            val jsonObject = JSONObject(bufferedReader.readLine())

                            if (
                                !jsonObject.getString(JSONHelper.JSONFields.TYPE_NAME)
                                    .equals(JSONHelper.GAME_INFO)
                            ) continue

                            val jsonPlayerArray =
                                jsonObject.getJSONArray(JSONHelper.JSONFields.PLAYERS_ARRAY_NAME)

                            val players = ArrayList<Player>()

                            for (i in 0 until jsonPlayerArray.length()) {
                                val name = jsonPlayerArray.getJSONObject(i)
                                    .getString(JSONHelper.JSONFields.PLAYER_NAME_NAME)

                                val encodedString = jsonPlayerArray.getJSONObject(i).getString(
                                    JSONHelper.JSONFields.AVATAR_NAME
                                )
                                val decodedString = Base64.decode(encodedString!!, Base64.URL_SAFE)
                                val avatar = BitmapFactory.decodeByteArray(
                                    decodedString,
                                    0,
                                    decodedString.size
                                )

                                val player = Player(name)
                                player.avatar = avatar
                                player.score = jsonPlayerArray.getJSONObject(i).getInt(JSONHelper.JSONFields.SCORE_NAME)

                                players.add(player)
                            }

                            gameModel.players = players

                            val gameBoard = ArrayList<Int>()
                            val jsonBoard = jsonObject.getJSONArray(JSONHelper.JSONFields.BOARD_NAME)
                            for (i in 0 until jsonBoard.length()) {
                                gameBoard.add(jsonBoard.getInt(i))
                            }

                            gameModel.board = gameBoard
                            gameModel.currentPlayer.postValue(jsonObject.getInt(JSONHelper.JSONFields.CURRENT_PLAYER_NAME))

                            gameModel.gameState.postValue(
                                when (jsonObject.getString(JSONHelper.JSONFields.GAME_STATE_NAME)) {
                                    JSONHelper.GAME_STATE_PLAYING -> GameState.PLAYING
                                    JSONHelper.GAME_STATE_FINISHED -> GameState.FINISHED

                                    else -> GameState.FINISHED
                                }
                            )

                            Handler(Looper.getMainLooper()).post {
                                recyclerViewAdapter.notifyDataSetChanged()
                            }
                        } catch (_: Exception) {
                        }
                    }
                }
            }
        } else {
            for (player: Player in GameModel.players) {
                if (player.socket == null) continue

                thread {
                    // player.socket!!.soTimeout = 1000

                    player.socket!!.getInputStream()?.run {
                        while (GameModel.gameState.value != GameState.FINISHED) {
                            try {
                                val bufferedReader = this.bufferedReader()

                                val jsonObject = JSONObject(bufferedReader.readLine())

                                if (
                                    !jsonObject.getString(JSONHelper.JSONFields.TYPE_NAME)
                                        .equals(JSONHelper.MOVE)
                                ) continue

                                player.currentPiece = when (jsonObject.getString(JSONHelper.JSONFields.TYPE_PIECE_NAME)) {
                                    JSONHelper.NORMAL_PIECE -> TypePiece.NORMAL
                                    JSONHelper.BOMB_PIECE -> TypePiece.BOMB
                                    JSONHelper.SWAP_PIECE -> {
                                        val jsonSwapPositionsArray = jsonObject.getJSONArray(JSONHelper.JSONFields.SWAP_POSITIONS_ARRAY_NAME)

                                        for (i in 0 until jsonSwapPositionsArray.length()) {
                                            gameModel.swapPositionArray.add(jsonSwapPositionsArray.getInt(1))
                                        }

                                        TypePiece.SWAP
                                    }
                                    JSONHelper.SKIP_PIECE -> {
                                        gameModel.nextPlayer()
                                        TypePiece.NORMAL
                                        continue
                                    }

                                    else -> TypePiece.NORMAL
                                }

                                val position = jsonObject.getInt(JSONHelper.JSONFields.POSITION_NAME)

                                if (gameModel.players.indexOf(player) == gameModel.currentPlayer.value) {
                                    makeMove(position)
                                }
                            } catch (e: Exception) {
                                Log.wtf("ServerClientReadingThread", "" + e)
                            }
                        }
                    }
                }
            }
        }
    }

    fun makeMove(position: Int) {
        if (isClient) {
            val jsonObject = JSONObject()

            jsonObject.put(JSONHelper.JSONFields.TYPE_NAME, JSONHelper.MOVE)

            jsonObject.put(JSONHelper.JSONFields.TYPE_PIECE_NAME, when (gameModel.players[myPlayerNumber].currentPiece) {
                TypePiece.NORMAL -> JSONHelper.NORMAL_PIECE
                TypePiece.BOMB -> JSONHelper.BOMB_PIECE
                TypePiece.SWAP -> {

                }

                else -> JSONHelper.NORMAL_PIECE
            })

            jsonObject.put(JSONHelper.JSONFields.POSITION_NAME, position)

            thread {
                gameModel.serverSocket!!.getOutputStream()?.run {
                    try {
                        val printStream = PrintStream(this)

                        printStream.println(jsonObject.toString())
                        printStream.flush()
                    } catch (e: Exception) {
                        Log.wtf("PThread", "" + e)
                    }
                }
            }
        } else {
            gameModel.makeMove(position)

            val jsonObject = JSONObject()

            jsonObject.put(JSONHelper.JSONFields.TYPE_NAME, JSONHelper.GAME_INFO)

            val jsonPlayerArray = JSONArray()

            for (player: Player in gameModel.players) {
                jsonPlayerArray.put(player.getAsJsonObject())
            }

            jsonObject.put(JSONHelper.JSONFields.PLAYERS_ARRAY_NAME, jsonPlayerArray)
            jsonObject.put(JSONHelper.JSONFields.BOARD_NAME, JSONArray(GameModel.board))
            jsonObject.put(JSONHelper.JSONFields.CURRENT_PLAYER_NAME, GameModel.currentPlayer.value)
            jsonObject.put(JSONHelper.JSONFields.GAME_STATE_NAME, when (GameModel.gameState.value) {
                GameState.PLAYING -> JSONHelper.GAME_STATE_PLAYING
                GameState.FINISHED -> JSONHelper.GAME_STATE_FINISHED

                else -> JSONHelper.GAME_STATE_FINISHED
            })

            for (player: Player in gameModel.players) {
                if (player.socket == null) continue

                thread {
                    player.socket!!.getOutputStream()?.run {
                        try {
                            val printStream = PrintStream(this)

                            printStream.println(jsonObject.toString())
                            printStream.flush()
                        } catch (e: Exception) {
                            Log.wtf("PThread", "" + e)
                        }
                    }
                }
            }

            Handler(Looper.getMainLooper()).post {
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }

    fun getAllPossibleMoves(): List<Int> {
        if (myPlayerNumber == -1) return ArrayList()

        return gameModel.getAllPossibleMoves(myPlayerNumber)
    }

    fun updateFirebase() {
        db = Firebase.firestore

        val collection = db.collection("TopScores")

        collection
            .orderBy("score", Query.Direction.ASCENDING)
            .whereEqualTo("player", gameModel.players[0].name)
            .get()
            .addOnSuccessListener {
                if (it.size() < 5) {
                    collection.document()
                        .set(
                            if (gameModel.players.size == 3) {
                                hashMapOf(
                                    "player" to gameModel.players[0].name,
                                    "score" to gameModel.players[0].score,
                                    "opponent_one" to gameModel.players[1].name,
                                    "opponent_one_score" to gameModel.players[1].score,
                                    "opponent_two" to gameModel.players[2].name,
                                    "opponent_two_score" to gameModel.players[2].score
                                )
                            }
                            else {
                                hashMapOf(
                                    "player" to gameModel.players[0].name,
                                    "score" to gameModel.players[0].score,
                                    "opponent_one" to gameModel.players[1].name,
                                    "opponent_one_score" to gameModel.players[1].score,
                                    "opponent_two" to "",
                                    "opponent_two_score" to 0
                                )
                            }
                        )
                } else {
                    if (it.documents[0].getLong("score")!! >= gameModel.players[0].score) return@addOnSuccessListener

                    it.documents[0].reference.delete()
                    collection.document()
                        .set(
                            if (gameModel.players.size == 3) {
                                hashMapOf(
                                    "player" to gameModel.players[0].name,
                                    "score" to gameModel.players[0].score,
                                    "opponent_one" to gameModel.players[1].name,
                                    "opponent_one_score" to gameModel.players[1].score,
                                    "opponent_two" to gameModel.players[2].name,
                                    "opponent_two_score" to gameModel.players[2].score
                                )
                            }
                            else {
                                hashMapOf(
                                    "player" to gameModel.players[0].name,
                                    "score" to gameModel.players[0].score,
                                    "opponent_one" to gameModel.players[1].name,
                                    "opponent_one_score" to gameModel.players[1].score,
                                    "opponent_two" to "",
                                    "opponent_two_score" to 0
                                )
                            }
                        )
                }

            }


        collection
            .orderBy("score", Query.Direction.ASCENDING)
            .whereEqualTo("player", gameModel.players[1].name)
            .get()
            .addOnSuccessListener {
                if (it.size() < 5) {
                    collection.document()
                        .set(
                            if (gameModel.players.size == 3) {
                                hashMapOf(
                                    "player" to gameModel.players[1].name,
                                    "score" to gameModel.players[1].score,
                                    "opponent_one" to gameModel.players[0].name,
                                    "opponent_one_score" to gameModel.players[0].score,
                                    "opponent_two" to gameModel.players[2].name,
                                    "opponent_two_score" to gameModel.players[2].score
                                )
                            }
                            else {
                                hashMapOf(
                                    "player" to gameModel.players[1].name,
                                    "score" to gameModel.players[1].score,
                                    "opponent_one" to gameModel.players[0].name,
                                    "opponent_one_score" to gameModel.players[0].score,
                                    "opponent_two" to "",
                                    "opponent_two_score" to 0
                                )
                            }
                        )
                } else {
                    if (it.documents[0].getLong("score")!! >= gameModel.players[0].score) return@addOnSuccessListener

                    it.documents[0].reference.delete()
                    collection.document()
                        .set(
                            if (gameModel.players.size == 3) {
                                hashMapOf(
                                    "player" to gameModel.players[1].name,
                                    "score" to gameModel.players[1].score,
                                    "opponent_one" to gameModel.players[0].name,
                                    "opponent_one_score" to gameModel.players[0].score,
                                    "opponent_two" to gameModel.players[2].name,
                                    "opponent_two_score" to gameModel.players[2].score
                                )
                            }
                            else {
                                hashMapOf(
                                    "player" to gameModel.players[1].name,
                                    "score" to gameModel.players[1].score,
                                    "opponent_one" to gameModel.players[0].name,
                                    "opponent_one_score" to gameModel.players[0].score,
                                    "opponent_two" to "",
                                    "opponent_two_score" to 0
                                )
                            }
                        )
                }
            }

        if (gameModel.players.size < 3) return

        collection
            .orderBy("score", Query.Direction.ASCENDING)
            .whereEqualTo("player", gameModel.players[2].name)
            .get()
            .addOnSuccessListener {
                if (it.size() < 5) {
                    collection.document()
                        .set(
                            hashMapOf(
                                "player" to gameModel.players[2].name,
                                "score" to gameModel.players[2].score,
                                "opponent_one" to gameModel.players[1].name,
                                "opponent_one_score" to gameModel.players[1].score,
                                "opponent_two" to gameModel.players[0].name,
                                "opponent_two_score" to gameModel.players[0].score
                            )
                        )
                } else {
                    if (it.documents[0].getLong("score")!! >= gameModel.players[0].score) return@addOnSuccessListener

                    it.documents[0].reference.delete()
                    collection.document()
                        .set(
                            hashMapOf(
                                "player" to gameModel.players[2].name,
                                "score" to gameModel.players[2].score,
                                "opponent_one" to gameModel.players[1].name,
                                "opponent_one_score" to gameModel.players[1].score,
                                "opponent_two" to gameModel.players[0].name,
                                "opponent_two_score" to gameModel.players[0].score
                            )
                        )
                }
            }
    }
}