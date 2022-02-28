package pt.isec.amov.ajp.reversi.game.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.isec.amov.ajp.reversi.R
import pt.isec.amov.ajp.reversi.data.Constants
import pt.isec.amov.ajp.reversi.data.GameState
import pt.isec.amov.ajp.reversi.databinding.ActivityTwoPlayerGameBinding
import pt.isec.amov.ajp.reversi.game.*

class NetworkTwoPlayerGameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTwoPlayerGameBinding
    private lateinit var viewModel : NetworkGameViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTwoPlayerGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gameRadioButtonGroup

        binding.imageViewAvatarOne?.setBackgroundColor(getColor(R.color.dracula_cyan))
        binding.imageViewAvatarTwo?.setBackgroundColor(getColor(R.color.dracula_red))

        viewModel = ViewModelProvider(this).get(NetworkGameViewModel::class.java)
        viewModel.gameModel.isOnline = true
        viewModel.isClient = intent.getBooleanExtra(Constants.INTENT_IS_CLIENT, false)
        viewModel.myPlayerNumber = intent.getIntExtra(Constants.INTENT_PLAYER_NUMBER, -1)

        viewModel.gameModel.currentPlayer.observe(this, observerCurrentPlayer())
        viewModel.gameModel.gameState.observe(this, observerGameState())

        recyclerView = binding.boardRecyclerView
        val recyclerViewAdapter = NetworkRecyclerViewAdapter(this)
        recyclerViewAdapter.gameViewModel = viewModel
        viewModel.recyclerViewAdapter = recyclerViewAdapter
        viewModel.prepareThreads()

        recyclerView.layoutManager = object : GridLayoutManager(this, BOARD_SIZE) {
            override fun canScrollHorizontally(): Boolean = false
            override fun canScrollVertically(): Boolean = false
        }
        recyclerView.adapter = recyclerViewAdapter

        if (GameModel.players[0].avatar != null) {
            binding.imageViewAvatarOne?.setImageBitmap(GameModel.players[0].avatar)
        }

        if (GameModel.players[1].avatar != null) {
            binding.imageViewAvatarTwo?.setImageBitmap(GameModel.players[1].avatar)
        }

        binding.gameRadioButtonGroup?.setOnCheckedChangeListener { radioGroup, checkedId ->
            val currentPlayer = GameModel.players[GameModel.currentPlayer.value!!]

            GameModel.players[GameModel.currentPlayer.value!!].currentPiece = when (checkedId) {
                R.id.normal_piece -> TypePiece.NORMAL
                R.id.bomb_piece -> {
                    if (currentPlayer.bombPiece) {
                        TypePiece.BOMB
                    } else {
                        radioGroup.check(R.id.normal_piece)
                        Toast.makeText(this, R.string.piece_unavailable, Toast.LENGTH_LONG).show()
                        TypePiece.NORMAL
                    }
                }
                R.id.swap_piece -> {
                    if (currentPlayer.swapPiece) {
                        TypePiece.SWAP
                    } else {
                        radioGroup.check(R.id.normal_piece)
                        Toast.makeText(this, R.string.piece_unavailable, Toast.LENGTH_LONG).show()
                        TypePiece.NORMAL
                    }
                }
                else -> {
                    Log.wtf(
                        "gameRadioButtonGroup.setOnCheckedChangeListener",
                        "UNEXPECTED ID: " + checkedId
                    )
                    TypePiece.NORMAL
                }
            }
        }

        binding.skipTurnButton?.setOnClickListener {
            recyclerViewAdapter.notifyDataSetChanged()
            GameModel.nextPlayer()
        }

        updateView()
    }

    private fun observerGameState() = Observer<GameState> {
        if (it != GameState.FINISHED) return@Observer

        var winner = viewModel.gameModel.players[0]
        for (player in viewModel.gameModel.players) {
            if (player.score > winner.score) winner = player
        }

        viewModel.updateFirebase()

        AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog)
            .setTitle(resources.getString(R.string.end_game))
            .setMessage(String.format(getString(R.string.end_game_message), winner.name, winner.score))
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                finish()
            }
            .show()
    }

    private fun observerCurrentPlayer() = Observer<Int> {
        binding.gameRadioButtonGroup?.check(R.id.normal_piece)
        updateView()
    }

    fun updateView() {
        binding.gameRadioButtonGroup?.check(R.id.normal_piece)

        binding.nameOne?.text = GameModel.players[0].name
        binding.nameTwo?.text = GameModel.players[1].name

        binding.nameOne?.setTextColor(this.getColor(R.color.dracula_foreground))
        binding.nameTwo?.setTextColor(this.getColor(R.color.dracula_foreground))

        when (GameModel.currentPlayer.value) {
            0 -> binding.nameOne?.setTextColor(this.getColor(R.color.dracula_green))
            1 -> binding.nameTwo?.setTextColor(this.getColor(R.color.dracula_green))
        }
    }

    companion object {
        const val BOARD_SIZE = 8
    }
}