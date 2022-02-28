package pt.isec.amov.ajp.reversi

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.text.Spanned
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import pt.isec.amov.ajp.reversi.data.Constants
import pt.isec.amov.ajp.reversi.data.GameState
import pt.isec.amov.ajp.reversi.data.JSONHelper
import pt.isec.amov.ajp.reversi.data.Player
import pt.isec.amov.ajp.reversi.database.ReversiLocalReader
import pt.isec.amov.ajp.reversi.databinding.ActivityMainBinding
import pt.isec.amov.ajp.reversi.game.GameModel
import pt.isec.amov.ajp.reversi.game.activities.LocalTwoPlayerGameActivity
import pt.isec.amov.ajp.reversi.game.activities.NetworkThreePlayerGameActivity
import pt.isec.amov.ajp.reversi.game.activities.NetworkTwoPlayerGameActivity
import java.io.PrintStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread
import kotlin.math.roundToInt
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ReversiLocalReader.filesDir = filesDir
        ReversiLocalReader.createIfUnavailable()
    }

    override fun onResume() {
        super.onResume()
        ReversiLocalReader.filesDir = filesDir
        ReversiLocalReader.createIfUnavailable()
    }

    override fun onRestart() {
        super.onRestart()
        ReversiLocalReader.filesDir = filesDir
        ReversiLocalReader.createIfUnavailable()
    }

    fun onClickTwoPlayersLocal(view: View) {
        GameModel.setupGameAsync(
            8,
            arrayOf(Player(ReversiLocalReader.getName()), Player(getString(R.string.anonymous)))
        )
        val intent = Intent(this, LocalTwoPlayerGameActivity::class.java)
        startActivity(intent)
    }

    fun reload(){
        recreate()
    }

    fun onClickTwoPlayersNetwork(view: View) {
        networkDialog(2)
    }

    fun onClickThreePlayersNetwork(view: View) = Toast
        .makeText(this, R.string.wip, Toast.LENGTH_LONG)
        .show()

    fun onClickSettings(view: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun networkDialog(nrPlayers: Int) {
        AlertDialog.Builder(this)
            .setTitle(R.string.two_player_network)
            .setMessage(R.string.client_server_question)
            .setPositiveButton(R.string.server) { _, _ ->
                when (nrPlayers) {
                    2 -> twoPlayerServerDialog()
                    3 -> threePlayerServerDialog()

                    else -> Log.wtf("NetDialog", "Invalid number of players: $nrPlayers")
                }
            }
            .setNegativeButton(R.string.client) { _, _ ->
                val editTextBox = EditText(this).apply {
                    maxLines = 1
                    filters = arrayOf(object : InputFilter {
                        override fun filter(
                            source: CharSequence?,
                            start: Int,
                            end: Int,
                            dest: Spanned?,
                            dstart: Int,
                            dend: Int
                        ): CharSequence? {
                            source?.run {
                                var ret = ""
                                forEach {
                                    if (it.isDigit() || it.equals('.'))
                                        ret += it
                                }
                                return ret
                            }
                            return null
                        }

                    })
                }

                AlertDialog.Builder(this)
                    .setTitle(R.string.client)
                    .setMessage(R.string.ask_server_ip)
                    .setPositiveButton(R.string.connect) { _, _ ->
                        val strIP = editTextBox.text.toString()

                        if (strIP.isEmpty() || !Patterns.IP_ADDRESS.matcher(strIP).matches()) {
                            Toast.makeText(this, getString(R.string.error_address), Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            val mePlayer = Player(ReversiLocalReader.getName())
                            mePlayer.avatar = BitmapFactory.decodeFile(ReversiLocalReader.getAvatarPath())

                            thread {
                                GameModel.serverSocket = Socket()
                                GameModel.serverSocket!!.connect(InetSocketAddress(strIP, Constants.SERVER_PORT))

                                GameModel.serverSocket!!.getOutputStream()?.run {
                                    try {
                                        val printStream = PrintStream(this)

                                        printStream.println(mePlayer.getAsJsonObject())
                                        printStream.flush()
                                    } catch (_: java.lang.Exception) {
                                    }
                                }

                                GameModel.serverSocket!!.getInputStream()?.run {
                                    try {
                                        val bufferedReader = this.bufferedReader()

                                        val jsonObject = JSONObject(bufferedReader.readLine())

                                        val jsonPlayerArray = jsonObject.getJSONArray(JSONHelper.JSONFields.PLAYERS_ARRAY_NAME)

                                        val players = ArrayList<Player>()

                                        for (i in 0 until jsonPlayerArray.length()) {
                                            val name = jsonPlayerArray.getJSONObject(i).getString(JSONHelper.JSONFields.PLAYER_NAME_NAME)

                                            val encodedString = jsonPlayerArray.getJSONObject(i).getString(JSONHelper.JSONFields.AVATAR_NAME)
                                            val decodedString = Base64.decode(encodedString!!, Base64.URL_SAFE)
                                            val avatar = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                                            val player = Player(name)
                                            player.avatar = avatar
                                            player.score = jsonPlayerArray.getJSONObject(i).getInt(JSONHelper.JSONFields.SCORE_NAME)

                                            players.add(player)
                                        }

                                        GameModel.setupGame(players.toTypedArray())

                                        val gameBoard = ArrayList<Int>()
                                        val jsonBoard = jsonObject.getJSONArray(JSONHelper.JSONFields.BOARD_NAME)
                                        for (i in 0 until jsonBoard.length()) {
                                            gameBoard.add(jsonBoard.getInt(i))
                                        }

                                        GameModel.board = gameBoard
                                        GameModel.boardSize = sqrt(gameBoard.size.toDouble()).roundToInt()
                                        GameModel.currentPlayer.postValue(jsonObject.getInt(JSONHelper.JSONFields.CURRENT_PLAYER_NAME))

                                        GameModel.gameState.postValue(when (jsonObject.getString(JSONHelper.JSONFields.GAME_STATE_NAME)) {
                                            JSONHelper.GAME_STATE_PLAYING -> GameState.PLAYING
                                            JSONHelper.GAME_STATE_FINISHED -> GameState.FINISHED

                                            else -> GameState.FINISHED
                                        })

                                        if (GameModel.players.size == 2) {
                                            Handler(Looper.getMainLooper()).post {
                                                val intent = Intent(this@MainActivity, NetworkTwoPlayerGameActivity::class.java)
                                                intent.putExtra(Constants.INTENT_IS_CLIENT, true)
                                                intent.putExtra(Constants.INTENT_PLAYER_NUMBER, GameModel.players.indexOf(mePlayer))
                                                startActivity(intent)
                                            }
                                        } else {
                                            Handler(Looper.getMainLooper()).post {
                                                val intent = Intent(this@MainActivity, NetworkThreePlayerGameActivity::class.java)
                                                intent.putExtra(Constants.INTENT_IS_CLIENT, true)
                                                intent.putExtra(Constants.INTENT_PLAYER_NUMBER, GameModel.players.indexOf(mePlayer))
                                                startActivity(intent)
                                            }
                                        }
                                    } catch (_: Exception) {
                                    }
                                }
                            }
                      }
                    }
                    .setView(editTextBox)
                    .create()
                    .show()
            }
            .show()
    }

    fun twoPlayerServerDialog() {
        val players = ArrayList<Player>()

        val mePlayer = Player(ReversiLocalReader.getName())
        mePlayer.avatar = BitmapFactory.decodeFile(ReversiLocalReader.getAvatarPath())

        players.add(mePlayer)

        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = wifiManager.connectionInfo.ipAddress // Deprecated in API Level 31. Suggestion NetworkCallback
        val strIPAddress = String.format("%d.%d.%d.%d",
            ip and 0xff,
            (ip shr 8) and 0xff,
            (ip shr 16) and 0xff,
            (ip shr 24) and 0xff
        )

        val linearLayout = LinearLayout(this).apply {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            this.setPadding(50, 50, 50, 50)
            layoutParams = params
            orientation = LinearLayout.HORIZONTAL
            addView(ProgressBar(context).apply {
                isIndeterminate = true
                val paramsPB = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                paramsPB.gravity = Gravity.CENTER_VERTICAL
                layoutParams = paramsPB
                indeterminateTintList = ColorStateList.valueOf(getColor(R.color.dracula_purple))
            })
            addView(TextView(context).apply {
                val paramsTV = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams = paramsTV
                text = String.format(getString(R.string.ip_address_message), strIPAddress + '\n')
                setTextColor(getColor(R.color.dracula_foreground))
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            })
        }

        val serverSocket = ServerSocket(Constants.SERVER_PORT)

        thread {
            serverSocket
                .use {
                    while (players.size < 2) {
                        try {
                            val socketClient = it.accept()

                            players.add(Player(socketClient))
                        } catch (_: Exception) {
                        }
                    }

                    GameModel.setupGameAsync(8, players.toTypedArray())

                    val jsonObject = JSONObject()

                    jsonObject.put(JSONHelper.JSONFields.TYPE_NAME, JSONHelper.GAME_INFO)

                    val jsonPlayerArray = JSONArray()

                    for (player: Player in GameModel.players) {
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

                    for (player: Player in players) {
                        if (player.socket == null) continue

                        player.socket!!.getOutputStream()?.run {
                            try {
                                val printStream = PrintStream(this)

                                printStream.println(jsonObject.toString())
                                printStream.flush()
                            } catch (_: Exception) {
                            }
                        }
                    }

                    Handler(Looper.getMainLooper()).post {
                        val intent = Intent(this, NetworkTwoPlayerGameActivity::class.java)
                        intent.putExtra(Constants.INTENT_IS_CLIENT, false)
                        intent.putExtra(Constants.INTENT_PLAYER_NUMBER, GameModel.players.indexOf(mePlayer))
                        startActivity(intent)
                    }
                }
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.server)
            .setView(linearLayout)
            .setCancelable(true)
            .setOnCancelListener {
                serverSocket.close()
            }
            .create()
            .show()
    }

    fun threePlayerServerDialog() {
        val players = ArrayList<Player>()

        val mePlayer = Player(ReversiLocalReader.getName())
        mePlayer.avatar = BitmapFactory.decodeFile(ReversiLocalReader.getAvatarPath())

        players.add(mePlayer)

        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = wifiManager.connectionInfo.ipAddress // Deprecated in API Level 31. Suggestion NetworkCallback
        val strIPAddress = String.format("%d.%d.%d.%d",
            ip and 0xff,
            (ip shr 8) and 0xff,
            (ip shr 16) and 0xff,
            (ip shr 24) and 0xff
        )

        val linearLayout = LinearLayout(this).apply {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            this.setPadding(50, 50, 50, 50)
            layoutParams = params
            orientation = LinearLayout.HORIZONTAL
            addView(ProgressBar(context).apply {
                isIndeterminate = true
                val paramsPB = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                paramsPB.gravity = Gravity.CENTER_VERTICAL
                layoutParams = paramsPB
                indeterminateTintList = ColorStateList.valueOf(getColor(R.color.dracula_purple))
            })
            addView(TextView(context).apply {
                val paramsTV = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams = paramsTV
                text = String.format(getString(R.string.ip_address_message), strIPAddress + '\n')
                setTextColor(getColor(R.color.dracula_foreground))
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            })
        }

        val serverSocket = ServerSocket(Constants.SERVER_PORT)

        thread {
            serverSocket
                .use {
                    while (players.size < 3) {
                        try {
                            val socketClient = it.accept()

                            players.add(Player(socketClient))
                        } catch (_: Exception) {
                        }
                    }

                    GameModel.setupGameAsync(10, players.toTypedArray())

                    val jsonObject = JSONObject()

                    jsonObject.put(JSONHelper.JSONFields.TYPE_NAME, JSONHelper.GAME_INFO)

                    val jsonPlayerArray = JSONArray()

                    for (player: Player in GameModel.players) {
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

                    for (player: Player in players) {
                        if (player.socket == null) continue

                        player.socket!!.getOutputStream()?.run {
                            try {
                                val printStream = PrintStream(this)

                                printStream.println(jsonObject.toString())
                                printStream.flush()
                            } catch (_: Exception) {
                            }
                        }
                    }

                    Handler(Looper.getMainLooper()).post {
                        val intent = Intent(this, NetworkThreePlayerGameActivity::class.java)
                        intent.putExtra(Constants.INTENT_IS_CLIENT, false)
                        intent.putExtra(Constants.INTENT_PLAYER_NUMBER, GameModel.players.indexOf(mePlayer))
                        startActivity(intent)
                    }
                }
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.server)
            .setView(linearLayout)
            .setCancelable(true)
            .setOnCancelListener {
                serverSocket.close()
            }
            .create()
            .show()
    }
}