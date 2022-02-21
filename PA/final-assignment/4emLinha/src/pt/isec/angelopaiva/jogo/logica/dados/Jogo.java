package pt.isec.angelopaiva.jogo.logica.dados;

import pt.isec.angelopaiva.jogo.logica.JogoStates;
import pt.isec.angelopaiva.jogo.logica.TypePiece;
import pt.isec.angelopaiva.jogo.logica.dados.minigames.IMinigame;
import pt.isec.angelopaiva.jogo.logica.dados.minigames.MinigameFactory;
import pt.isec.angelopaiva.jogo.logica.dados.players.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Jogo implements Serializable {
    public static final int NR_COLUMNS = 7;
    public static final int NR_ROWS = 6;
    public static final int NR_PLAYERS = 2;

    private int currentPlayer;
    private final List<Player> playersList;
    private JogoStates state;
    private TypePiece[][] board;

    transient IMinigame currentMinigame;

    public Jogo() {
        playersList = new ArrayList<>(NR_PLAYERS);
        startJogo();
    }

    public void startJogo() {
        currentPlayer = ThreadLocalRandom.current().nextInt(2); // random int between [0, 2[
        state = JogoStates.UNFINISHED;
        board = new TypePiece[NR_ROWS][NR_COLUMNS];

        for (TypePiece[] row : board) Arrays.fill(row, TypePiece.NONE);
    }

    public void setPlayers(Player p1, Player p2) {
        playersList.add(0, p1);
        playersList.add(1, p2);
    }

    public JogoStates getState() { return state; }

    //
    // Update
    //

    public void updateJogo() {
        updateRoundForCurrentPlayer();
        updateCurrentPlayer();
        updateState();
    }

    public void setCurrentPlayer(int currentPlayer) { this.currentPlayer = currentPlayer - 1; }

    public void updateCurrentPlayer() {
        currentPlayer++;

        if (currentPlayer == NR_PLAYERS) currentPlayer = 0;
    }

    private void updateState() {
        for (int i = 0; i < board[0].length; i++) {
            if (board[0][i] == TypePiece.NONE) break;

            if (i == (board[0].length - 1) && board[0][i] != TypePiece.NONE) {
                state = JogoStates.DRAW;
                return;
            }
        }

        if (checkWinForTypePiece(TypePiece.PIECE_P1)) {
            state = JogoStates.VICTORY_P1;
            return;
        }

        if (checkWinForTypePiece(TypePiece.PIECE_P2)) {
            state = JogoStates.VICTORY_P2;
            return;
        }

        state = JogoStates.UNFINISHED;
    }

    //
    // Check Win
    //

    private boolean checkWinForTypePiece(TypePiece player) {
        return checkHorizontalWinForTypePiece(player)
        || checkVerticalWinForTypePiece(player)
        || checkDiagonalWinForTypePiece(player);
    }

    private boolean checkHorizontalWinForTypePiece(TypePiece player) {
        // Vitória Horizontal (linhas)
        // i = linha
        // j = coluna
        for (int i = 0; i < getNumberBoardRows(); i++) {
            for (int j = 0; j < getNumberBoardColumns() - 3; j++) {
                if (this.board[i][j] == player && this.board[i][j + 1] == player
                && this.board[i][j + 2] == player && this.board[i][j + 3] == player) return true;
            }
        }

        return false;
    }

    private boolean checkVerticalWinForTypePiece(TypePiece player) {
        // Vitória Vertical (colunas)
        // i = linha
        // j = coluna
        for (int j = 0; j < getNumberBoardColumns(); j++) {
            for (int i = 0; i < getNumberBoardRows() - 3; i++) {
                if (this.board[i][j] == player && this.board[i + 1][j] == player
                && this.board[i + 2][j] == player && this.board[i + 3][j] == player) return true;
            }
        }

        return false;
    }

    private boolean checkDiagonalWinForTypePiece(TypePiece player) {
        // Vitória Diagonal Ascendente
        // i = linhas
        // j = colunas
        for (int j = 0; j < getNumberBoardColumns() - 3; j++) {
            for (int i = 3; i < getNumberBoardRows(); i++) {
                if (this.board[i][j] == player && this.board[i - 1][j + 1] == player
                && this.board[i - 2][j + 2] == player && this.board[i - 3][j + 3] == player) return true;
            }
        }

        // Vitória Diagonal Descendente
        // i = linhas
        // j = colunas
        for (int j = 0; j < getNumberBoardColumns() - 3; j++) {
            for (int i = 0; i < getNumberBoardRows() - 3; i++) {
                if (this.board[i][j] == player && this.board[i + 1][j + 1] == player
                && this.board[i + 2][j + 2] == player && this.board[i + 3][j + 3] == player) return true;
            }
        }

        return false;
    }

    //
    // Move Logic
    //

    public boolean placePieceOnColumn(int column) {
        for (int i = 1; i < board.length; i++) {
            if (board[i][column] != TypePiece.NONE) {
                board[i - 1][column] = currentPlayer == 0 ? TypePiece.PIECE_P1 : TypePiece.PIECE_P2;
                return true;
            }

            if (i == NR_ROWS - 1 && board[i][column] == TypePiece.NONE) {
                board[i][column] = currentPlayer == 0 ? TypePiece.PIECE_P1 : TypePiece.PIECE_P2;
                return true;
            }
        }

        return false;
    }

    public boolean removePieceFromColumn(int column) {
        for (TypePiece[] row : board) {
            if (row[column] != TypePiece.NONE) {
                row[column] = TypePiece.NONE;
                return true;
            }
        }

        return false;
    }

    public boolean placeSpecialPieceOnColumn(int column) {
        for (TypePiece[] row : board) row[column] = TypePiece.NONE;

        removeSpecialPieceFromCurrentPlayer();

        return true;
    }

    public boolean canPlacePieceOnColumn(int column) {
        if (!isColumnValid(column)) return false;

        return board[0][column] == TypePiece.NONE;
    }

    public boolean isColumnValid(int column) { return column >= 0 && column < getNumberBoardColumns(); }

    //
    // CurrentPlayer Methods
    //

    public int getCurrentPlayer() { return currentPlayer + 1; }

    public String getCurrentPlayerName() { return playersList.get(currentPlayer).getName(); }
    public boolean currentPlayerIsHuman() { return playersList.get(currentPlayer).isHuman(); }
    public boolean currentPlayerHasSpecialPiece() { return playersList.get(currentPlayer).getSpecialPiece() > 0; }

    public boolean currentPlayerCanPlayMinigames() {
        final int MINIMUM_ROUND = 4;

        return playersList.get(currentPlayer).getRound() >= MINIMUM_ROUND;
    }

    public int getUndoCreditsFromCurrentPlayer() { return playersList.get(currentPlayer).getUndoCredits(); }
    public int getMoveFromAI() { return playersList.get(currentPlayer).getRandomMove(0, getNumberBoardColumns()); }

    public void removeSpecialPieceFromCurrentPlayer() { playersList.get(currentPlayer).removeSpecialPiece(); }
    public void removeUndoCreditsFromCurrentPlayer(int nr) { playersList.get(currentPlayer).removeUndoCredits(nr); }
    public void giveCurrentPlayerSpecialPiece() { playersList.get(currentPlayer).addSpecialPiece(); }
    public void resetRoundForCurrentPlayer() { playersList.get(currentPlayer).resetRound(); }

    private void updateRoundForCurrentPlayer() { playersList.get(currentPlayer).updateRound(); }

    //
    // Player Specific Methods
    //

    public String getP1Name() { return playersList.get(0).getName(); }
    public boolean p1IsHuman() { return playersList.get(0).isHuman(); }
    public int getNumberSpecialPiecesP1() { return playersList.get(0).getSpecialPiece(); }
    public int getUndoCreditsP1() { return playersList.get(0).getUndoCredits(); }

    public String getP2Name() { return playersList.get(1).getName(); }
    public boolean p2IsHuman() { return playersList.get(1).isHuman(); }
    public int getNumberSpecialPiecesP2() { return playersList.get(1).getSpecialPiece(); }
    public int getUndoCreditsP2() { return playersList.get(1).getUndoCredits(); }

    //
    // Board Methods
    //

    public int getNumberBoardColumns() { return board[0].length; }
    public int getNumberBoardRows() { return board.length; }

    public String getBoardAsString() { final String ANSI_RED = "\u001B[31m";
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_BLUE = "\u001B[34m";
        final String ANSI_BLACK = "\u001B[30m";
        final String ANSI_RESET = "\u001B[0m";
        final String PIECE = "@";

        StringBuilder stringBuilder = new StringBuilder();

        for (TypePiece[] row : board) {
            for (TypePiece column : row) {
                stringBuilder.append(ANSI_BLUE + "|" + ANSI_RESET);

                switch (column) {
                    case NONE -> stringBuilder.append(ANSI_BLACK + PIECE + ANSI_RESET);
                    case PIECE_P1 -> stringBuilder.append(ANSI_RED + PIECE + ANSI_RESET);
                    case PIECE_P2 -> stringBuilder.append(ANSI_YELLOW + PIECE + ANSI_RESET);
                }
            }

            stringBuilder.append(ANSI_BLUE + "|\n" + ANSI_RESET);
        }
        return stringBuilder.toString();
    }

    public TypePiece[][] getCopyOfBoard() { return Arrays.copyOf(board, getNumberBoardRows()); }

    public List<TypePiece> getColumnAsList(int column) {
        List<TypePiece> columnList = new ArrayList<>();

        for (TypePiece[] row : board) columnList.add(row[column]);

        return columnList;
    }

    public void replaceColumnWithList(List<TypePiece> columnList, int column) {
        for (int i = 0; i < getNumberBoardRows(); i++) {
            board[i][column] = columnList.get(i);
        }
    }

    //
    // Get EndGame Info
    //

    public String getEndGameInfo() {
        if (state == JogoStates.DRAW) return "O jogo acabou em empate!";

        String returnString = "O jogador ";

        if (state == JogoStates.VICTORY_P1) returnString += playersList.get(0).getName();
        if (state == JogoStates.VICTORY_P2) returnString += playersList.get(1).getName();

        return returnString + " venceu o jogo!";
    }

    //
    // Minigames
    //

    public void prepareMinigame() { currentMinigame = MinigameFactory.getMinigame(); }

    public void setMinigameAnswer(String answer) { currentMinigame.setAnswer(answer); }

    public boolean isMinigameFinished() { return currentMinigame.isFinished(); }
    public boolean hasWonMinigame() { return currentMinigame.hasWon(); }
    
    public String getMinigameWording() { return currentMinigame.getWording(); }
    public String getMinigameQuestion() { return currentMinigame.getQuestion(); }
}
