package pt.isec.amov.ajp.reversi.data

object JSONHelper {
    // Type of JSON
    const val PLAYER_INFO = "PLAYER_INFO"
    const val GAME_INFO = "GAME_INFO"
    const val MOVE = "MOVE"

    // Types of Piece
    const val NORMAL_PIECE = "normalPiece"
    const val BOMB_PIECE = "bombPiece"
    const val SWAP_PIECE = "swapPiece"
    const val SKIP_PIECE = "skip"

    // State of Game
    const val GAME_STATE_PLAYING = "gameStatePlaying"
    const val GAME_STATE_FINISHED = "gameStateFinished"

    object JSONFields {
        const val TYPE_NAME = "type" // String

        const val GAME_STATE_NAME = "gameState" // String

        const val PLAYER_NAME_NAME = "playerName" // String
        const val AVATAR_NAME = "avatar" // Bitmap Encoded to String
        const val SCORE_NAME = "score" // Int

        const val TYPE_PIECE_NAME = "typePiece" // String
        const val POSITION_NAME = "position" // Int
        const val SWAP_POSITIONS_ARRAY_NAME = "swapPositionsArray" // Array of Int

        const val PLAYERS_ARRAY_NAME = "playersArray" // Array of Player
        const val CURRENT_PLAYER_NAME = "currentPlayer" // Int (position in playersArray)
        const val BOARD_NAME = "board" // Array of Int
    }
}