package pt.isec.amov.ajp.reversi.game

import androidx.lifecycle.MutableLiveData
import pt.isec.amov.ajp.reversi.data.GameState
import pt.isec.amov.ajp.reversi.data.Player
import java.net.Socket

object GameModel {
    private const val TAG = "GameModel"

    var isOnline: Boolean = false

    var serverSocket: Socket? = null

    var gameState = MutableLiveData(GameState.PLAYING)

    var board = ArrayList<Int>()
    var boardSize = 6

    var players = ArrayList<Player>()
    //var currentPlayer : Int = 0
    var currentPlayer = MutableLiveData(0)

    var swapPositionArray = arrayListOf<Int>()

    fun setupGame(newPlayers: Array<Player>) {
        board.clear()

        players.clear()
        players.addAll(newPlayers)
    }

    fun setupGameAsync(boardSize: Int, newPlayers: Array<Player>) {
        gameState.postValue(GameState.PLAYING)

        this.boardSize = boardSize
        board.clear()

        players.clear()
        players.addAll(newPlayers)

        setupBoard()
        updateScore()

        currentPlayer.postValue(randomPlayer())
    }

    fun setupGame(boardSize: Int, newPlayers: Array<Player>) {
        gameState.value = GameState.PLAYING

        this.boardSize = boardSize
        board.clear()

        players.clear()
        players.addAll(newPlayers)

        setupBoard()
        updateScore()

        currentPlayer.value = randomPlayer()
    }

    fun setupGame(boardSize: Int) {
        gameState.value = GameState.PLAYING

        this.boardSize = boardSize
        board.clear()

        players.clear()
        players.add(Player("Player 1"))
        players.add(Player("Player 2"))

        setupBoard()
        updateScore()

        currentPlayer.value = randomPlayer()
    }

    fun nextPlayer() {
        var currentPlayerTemp = currentPlayer.value!!
        currentPlayerTemp++

        if (currentPlayerTemp == players.size) currentPlayerTemp = 0
        currentPlayer.postValue(currentPlayerTemp)
    }

    private fun randomPlayer(): Int = (0 until players.size).random()

    private fun setupBoard() {
        for (i in 0 until boardSize * boardSize) {
            board.add(-1)
        }

        if (players.size == 2) {
            board[getIndex(3, 3)] = 0
            board[getIndex(3, 4)] = 1
            board[getIndex(4, 3)] = 1
            board[getIndex(4, 4)] = 0
        } else if (players.size == 3) {
            board[getIndex(2, 4)] = 0
            board[getIndex(2, 5)] = 1
            board[getIndex(3, 4)] = 1
            board[getIndex(3, 5)] = 0

            board[getIndex(6, 2)] = 2
            board[getIndex(6, 3)] = 0
            board[getIndex(7, 2)] = 0
            board[getIndex(7, 3)] = 2

            board[getIndex(6, 6)] = 1
            board[getIndex(6, 7)] = 2
            board[getIndex(7, 6)] = 2
            board[getIndex(7, 7)] = 1
        }
    }

    private fun updateScore() {
        for (player: Player in players) player.score = 0

        for (piece: Int in board) {
            if (piece == -1) continue

            players[piece].score++
        }
    }

    private fun isBombMoveLegal(position: Int) = board[position] == currentPlayer.value!!

    private fun isNormalMoveLegal(position : Int, currentPlayer : Int) : Boolean {
        val (row, column) = getPositionCoordinates(position)

        if (board[position] != -1) return false
        if (position < 0 || position >= board.size) return false

        var currentRow = row
        var currentColumn = column
        var countPieces = 0
        var otherPlayers = (0 until players.size).filter { it != currentPlayer }

        // Move Up
        currentRow = row - 1
        currentColumn = column
        countPieces = 0

        while (
            currentRow > 0
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            currentRow--
            countPieces++
        }

        if (
            currentRow >= 0
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
            && countPieces > 0
        ) return true

        // Move Down
        currentRow = row + 1
        currentColumn = column
        countPieces = 0

        while (
            currentRow < boardSize - 1
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            currentRow++
            countPieces++
        }

        if (
            currentRow <= boardSize - 1
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
            && countPieces > 0
        ) return true
        
        // Move Left
        currentRow = row
        currentColumn = column - 1
        countPieces = 0

        while (
            currentColumn > 0
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            currentColumn--
            countPieces++
        }

        if (
            currentColumn >= 0
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
            && countPieces > 0
        ) return true

        // Move Right
        currentRow = row
        currentColumn = column + 1
        countPieces = 0

        while (
            currentColumn < boardSize - 1
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            currentColumn++
            countPieces++
        }

        if (
            currentColumn <= boardSize - 1
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
            && countPieces > 0
        ) return true

        // Move Up Left
        currentRow = row - 1
        currentColumn = column - 1
        countPieces = 0

        while (
            currentRow > 0
            && currentColumn > 0
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            currentRow--
            currentColumn--
            countPieces++
        }

        if (
            currentRow >= 0
            && currentColumn >= 0
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
            && countPieces > 0
        ) return true

        // Move Up Right
        currentRow = row - 1
        currentColumn = column + 1
        countPieces= 0

        while (
            currentRow > 0
            && currentColumn < boardSize - 1
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            currentRow--
            currentColumn++
            countPieces++
        }

        if (
            currentRow >= 0
            && currentColumn<=boardSize - 1
            && board[getIndex(currentRow,currentColumn)] == currentPlayer
            && countPieces>0
        ) return true

        // Move Down Left
        currentRow = row + 1
        currentColumn = column - 1
        countPieces = 0

        while (
            currentRow < boardSize - 1
            && currentColumn > 0
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            currentRow++
            currentColumn--
            countPieces++
        }

        if (
            currentRow <= boardSize - 1
            && currentColumn >= 0
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
            && countPieces > 0
        ) return true

        // Move Down Right
        currentRow = row + 1
        currentColumn = column + 1
        countPieces = 0

        while (
            currentRow < boardSize - 1
            && currentColumn < boardSize - 1
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            currentRow++
            currentColumn++
            countPieces++
        }

        if (
            currentRow <= boardSize - 1
            && currentColumn <= boardSize - 1
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
            && countPieces > 0
        ) return true

        // When all hopes fade away
        return false
    }

    fun getPositionsToReverse(position : Int, currentPlayer: Int) : List<Int> {
        var positionsToReverse = ArrayList<Int>()

        val (row, column) = getPositionCoordinates(position)

        var currentRow = row
        var currentColumn = column
        var otherPlayers = (0 until players.size).filter { it != currentPlayer }
        var tinyPositionsToReverse = ArrayList<Int>()

        // Move Up
        tinyPositionsToReverse.clear()
        currentRow = row - 1
        currentColumn = column

        while (
            currentRow > 0
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            tinyPositionsToReverse.add(getIndex(currentRow, currentColumn))
            currentRow--
        }

        if (
            currentRow >= 0
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
        ) { positionsToReverse.addAll(tinyPositionsToReverse) }

        // Move Down
        tinyPositionsToReverse.clear()
        currentRow = row + 1
        currentColumn = column

        while (
            currentRow < boardSize - 1
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            tinyPositionsToReverse.add(getIndex(currentRow, currentColumn))
            currentRow++
        }

        if (
            currentRow <= boardSize - 1
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
        ) { positionsToReverse.addAll(tinyPositionsToReverse) }

        // Move Left
        tinyPositionsToReverse.clear()
        currentRow = row
        currentColumn = column - 1

        while (
            currentColumn > 0
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            tinyPositionsToReverse.add(getIndex(currentRow, currentColumn))
            currentColumn--
        }

        if (
            currentColumn >= 0
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
        ) { positionsToReverse.addAll(tinyPositionsToReverse) }

        // Move Right
        tinyPositionsToReverse.clear()
        currentRow = row
        currentColumn = column + 1

        while (
            currentColumn < boardSize - 1
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            tinyPositionsToReverse.add(getIndex(currentRow, currentColumn))
            currentColumn++
        }

        if (
            currentColumn <= boardSize - 1
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
        ) { positionsToReverse.addAll(tinyPositionsToReverse) }

        // Move Up Left
        tinyPositionsToReverse.clear()
        currentRow = row - 1
        currentColumn = column - 1

        while (
            currentRow > 0
            && currentColumn > 0
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            tinyPositionsToReverse.add(getIndex(currentRow, currentColumn))
            currentRow--
            currentColumn--
        }

        if (
            currentRow >= 0
            && currentColumn >= 0
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
        ) { positionsToReverse.addAll(tinyPositionsToReverse) }

        // Move Up Right
        tinyPositionsToReverse.clear()
        currentRow = row - 1
        currentColumn = column + 1

        while (
            currentRow > 0
            && currentColumn < boardSize - 1
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            tinyPositionsToReverse.add(getIndex(currentRow, currentColumn))
            currentRow--
            currentColumn++
        }

        if (
            currentRow >= 0
            && currentColumn<=boardSize - 1
            && board[getIndex(currentRow,currentColumn)] == currentPlayer
        ) { positionsToReverse.addAll(tinyPositionsToReverse) }

        // Move Down Left
        tinyPositionsToReverse.clear()
        currentRow = row + 1
        currentColumn = column - 1

        while (
            currentRow < boardSize - 1
            && currentColumn > 0
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            tinyPositionsToReverse.add(getIndex(currentRow, currentColumn))
            currentRow++
            currentColumn--
        }

        if (
            currentRow <= boardSize - 1
            && currentColumn >= 0
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
        ) { positionsToReverse.addAll(tinyPositionsToReverse) }

        // Move Down Right
        tinyPositionsToReverse.clear()
        currentRow = row + 1
        currentColumn = column + 1

        while (
            currentRow < boardSize - 1
            && currentColumn < boardSize - 1
            && otherPlayers.contains(board[getIndex(currentRow, currentColumn)])
        ) {
            tinyPositionsToReverse.add(getIndex(currentRow, currentColumn))
            currentRow++
            currentColumn++
        }

        if (
            currentRow <= boardSize - 1
            && currentColumn <= boardSize - 1
            && board[getIndex(currentRow, currentColumn)] == currentPlayer
        ) { positionsToReverse.addAll(tinyPositionsToReverse) }

        return positionsToReverse
    }

    fun getAllPossibleMoves(currentPlayer : Int): List<Int> {
        val result = ArrayList<Int>()

        for (position in 0 until board.size) {
            if (isNormalMoveLegal(position, currentPlayer)) { result.add(position) }
        }

        return result
    }

    fun getPositionCoordinates(position : Int) = position / boardSize to position % boardSize

    fun makeMove(position : Int) {
        val successfulMove = when (players[currentPlayer.value!!].currentPiece) {
            TypePiece.NORMAL -> makeNormalMove(position)
            TypePiece.BOMB -> makeBombMove(position)
            TypePiece.SWAP -> makeSwapMove(position)

            else -> false
        }

        if (successfulMove) {
            players[currentPlayer.value!!].currentPiece = TypePiece.NORMAL
            nextPlayer()
        }
        updateScore()
        updateGameState()
    }

    fun makeNormalMove(position: Int) : Boolean {
        if (!isNormalMoveLegal(position, currentPlayer.value!!)) return false

        board[position] = currentPlayer.value!!

        for (positionToReverse in getPositionsToReverse(position, currentPlayer.value!!)) {
            board[positionToReverse] = currentPlayer.value!!
        }

        return true
    }

    fun makeBombMove(position: Int) : Boolean {
        if (!players[currentPlayer.value!!].bombPiece) return false
        if (!isBombMoveLegal(position)) return false

        val (row, column) = getPositionCoordinates(position)

        board[position] = -1

        if (row > 0) {
            if (column > 0) board[getIndex(row - 1, column - 1)] = -1
            board[getIndex(row - 1, column)] = -1
            if (column < boardSize - 1) board[getIndex(row - 1, column + 1)] = -1
        }

        if (column > 0) board[getIndex(row, column - 1)] = -1
        board[getIndex(row, column)] = -1
        if (column < boardSize - 1) board[getIndex(row, column + 1)] = -1

        if (row < boardSize - 1) {
            if (column > 0) board[getIndex(row + 1, column - 1)] = -1
            board[getIndex(row + 1, column)] = -1
            if (column < boardSize - 1) board[getIndex(row + 1, column + 1)] = -1
        }

        players[currentPlayer.value!!].bombPiece = false
        return true
    }

    fun makeSwapMove(position: Int): Boolean {
        if (!players[currentPlayer.value!!].swapPiece) return false

        if (swapPositionArray.size < 2) {
            if (board[position] == currentPlayer.value!!) swapPositionArray.add(position)
            return false
        }

        if (board[position] == currentPlayer.value!! || board[position] == -1) return false

        for (swapPosition: Int in swapPositionArray) board[swapPosition] = -1
        board[position] = currentPlayer.value!!

        players[currentPlayer.value!!].swapPiece = false
        return true
    }

    fun updateGameState() {
        for (player in 0 until players.size)
            if (getAllPossibleMoves(player).isNotEmpty()) return

        gameState.postValue(GameState.FINISHED)

    }

    private fun getIndex(row: Int, column: Int) : Int = (boardSize * row) + column
}