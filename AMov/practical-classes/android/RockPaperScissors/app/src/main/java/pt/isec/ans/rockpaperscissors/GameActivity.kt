package pt.isec.ans.rockpaperscissors

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.*
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.postDelayed
import androidx.lifecycle.ViewModelProvider
import java.util.*

const val SERVER_MODE = 0
const val CLIENT_MODE = 1
const val TAG = "GameActivity"

class GameActivity : AppCompatActivity() {
    private lateinit var model: GameViewModel
    private lateinit var imgRock: ImageView
    private lateinit var imgPaper: ImageView
    private lateinit var imgScissors: ImageView
    private lateinit var tvInfo: TextView
    private var dlg: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        model = ViewModelProvider(this).get(GameViewModel::class.java)
        model.state.observe(this) {
            updateUI()
        }
        tvInfo = findViewById(R.id.tvInfo)
        imgRock = findViewById<ImageView>(R.id.imageRock).apply {
            setOnClickListener { model.changeMyMove(MOVE_ROCK) }
        }
        imgPaper = findViewById<ImageView>(R.id.imagePaper).apply {
            setOnClickListener { model.changeMyMove(MOVE_PAPER) }
        }
        imgScissors = findViewById<ImageView>(R.id.imageScissors).apply {
            setOnClickListener { model.changeMyMove(MOVE_SCISSORS) }
        }

        model.connectionState.observe(this) { state ->
            updateUI()
            if ( state != GameViewModel.ConnectionState.SETTING_PARAMETERS &&
                 state != GameViewModel.ConnectionState.SERVER_CONNECTING && dlg?.isShowing == true) {
                dlg?.dismiss()
                dlg = null
            }

            if (state == GameViewModel.ConnectionState.CONNECTION_ERROR) {
                finish()
            }
            if (state == GameViewModel.ConnectionState.CONNECTION_ENDED)
                finish()
        }

        if (model.connectionState.value != GameViewModel.ConnectionState.CONNECTION_ESTABLISHED) {
            when (intent.getIntExtra("mode", SERVER_MODE)) {
                SERVER_MODE -> startAsServer()
                CLIENT_MODE -> startAsClient()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //to do: should ask if the user wants to finish
        model.stopGame()
    }

    override fun onPause() {
        super.onPause()
        dlg?.apply {
            if (isShowing)
                dismiss()
        }
    }

    private fun updateUI() {
        if (model.connectionState.value != GameViewModel.ConnectionState.CONNECTION_ESTABLISHED) {
            tvInfo.visibility = View.INVISIBLE
            imgRock.visibility = View.INVISIBLE
            imgPaper.visibility = View.INVISIBLE
            imgScissors.visibility = View.INVISIBLE
            return
        }
        tvInfo.visibility = if (model.myMove == MOVE_NONE) View.VISIBLE else View.INVISIBLE
        imgRock.visibility = if (model.myMove == MOVE_NONE || model.myMove == MOVE_ROCK) View.VISIBLE else View.INVISIBLE
        imgPaper.visibility = if (model.myMove == MOVE_NONE || model.myMove == MOVE_PAPER) View.VISIBLE else View.INVISIBLE
        imgScissors.visibility = if (model.myMove == MOVE_NONE || model.myMove == MOVE_SCISSORS) View.VISIBLE else View.INVISIBLE

        if (model.myMove != MOVE_NONE && model.otherMove != MOVE_NONE) // if (model.state == ROUND_ENDED)...
            imgRock.postDelayed(5000) {
                model.startGame()
            }
    }

    private fun startAsServer() {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = wifiManager.connectionInfo.ipAddress // Deprecated in API Level 31. Suggestion NetworkCallback
        val strIPAddress = String.format("%d.%d.%d.%d",
                ip and 0xff,
                (ip shr 8) and 0xff,
                (ip shr 16) and 0xff,
                (ip shr 24) and 0xff
        )

        val ll = LinearLayout(this).apply {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            this.setPadding(50, 50, 50, 50)
            layoutParams = params
            setBackgroundColor(Color.rgb(240, 224, 208))
            orientation = LinearLayout.HORIZONTAL
            addView(ProgressBar(context).apply {
                isIndeterminate = true
                val paramsPB = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                paramsPB.gravity = Gravity.CENTER_VERTICAL
                layoutParams = paramsPB
                indeterminateTintList = ColorStateList.valueOf(Color.rgb(96, 96, 32))
            })
            addView(TextView(context).apply {
                val paramsTV = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams = paramsTV
                text = String.format(getString(R.string.msg_ip_address),strIPAddress)
                textSize = 20f
                setTextColor(Color.rgb(96, 96, 32))
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            })
        }

        dlg = AlertDialog.Builder(this).run {
            setTitle(getString(R.string.server_mode))
            setView(ll)
            setOnCancelListener {
                model.stopServer()
                finish()
            }
            create()
        }
        model.startServer()

        dlg?.show()
    }

    private fun startAsClient() {
        val edtBox = EditText(this).apply {
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
        val dlg = AlertDialog.Builder(this).run {
            setTitle(getString(R.string.client_mode))
            setMessage(getString(R.string.ask_ip))
            setPositiveButton(getString(R.string.button_connect)) { _: DialogInterface, _: Int ->
                val strIP = edtBox.text.toString()
                if (strIP.isEmpty() || !Patterns.IP_ADDRESS.matcher(strIP).matches()) {
                    Toast.makeText(this@GameActivity, getString(R.string.error_address), Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    model.startClient(edtBox.text.toString())
                }
            }
            setNeutralButton(getString(R.string.btn_emulator)) { _: DialogInterface, _: Int ->
                model.startClient("10.0.2.2", SERVER_PORT-1)
                // Configure port redirect on the Server Emulator:
                // telnet localhost <5554|5556|5558|...>
                // auth <key>
                // redir add tcp:9998:9999
            }
            setNegativeButton(getString(R.string.button_cancel)) { _: DialogInterface, _: Int ->
                finish()
            }
            setCancelable(false)
            setView(edtBox)
            create()
        }
        dlg.show()
    }

}