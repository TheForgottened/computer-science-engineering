package pt.isec.angelopaiva.jogo.logica;

import pt.isec.angelopaiva.jogo.logica.comandos.PlacePieceOnColumn;
import pt.isec.angelopaiva.jogo.logica.comandos.PlaceSpecialPieceOnColumn;
import pt.isec.angelopaiva.jogo.logica.comandos.UpdateJogo;
import pt.isec.angelopaiva.jogo.logica.dados.Jogo;
import pt.isec.angelopaiva.jogo.logica.dados.players.Player;

import java.io.Serializable;

public class JogoManager implements Serializable {
    private final Jogo jogo;
    private final CommandManager commandManager;
    private int firstPlayer;

    public JogoManager() {
        this.jogo = new Jogo();
        this.commandManager = new CommandManager();
    }

    //
    // Need CommandManager
    //

    public void placePieceOnColumn(int column) {
        commandManager.invokeCommand(new PlacePieceOnColumn(jogo, column));
        commandManager.invokeCommand(new UpdateJogo(jogo));
    }

    public void placeSpecialPieceOnColumn(int column) {
        commandManager.invokeCommand(new PlaceSpecialPieceOnColumn(jogo, column));
        commandManager.invokeCommand(new UpdateJogo(jogo));
    }

    public void updateJogo() { commandManager.invokeCommand(new UpdateJogo(jogo)); }

    public boolean undo() { return commandManager.undo(); }

    public int getMaxUndos() {
        return Math.min(jogo.getUndoCreditsFromCurrentPlayer(), commandManager.getNrCommandsForReplay());
    }

    //
    // Doesn't Need CommandManager
    //

    public void startJogo() {
        jogo.startJogo();
        firstPlayer = jogo.getCurrentPlayer();
    }

    public boolean canPlacePieceOnColumn(int column) { return jogo.canPlacePieceOnColumn(column); }

    public void setPlayers(Player p1, Player p2) { jogo.setPlayers(p1, p2); }

    // CurrentPlayer methods

    public int getCurrentPlayer() { return jogo.getCurrentPlayer(); }
    public String getCurrentPlayerName() { return jogo.getCurrentPlayerName(); }

    public boolean currentPlayerHasSpecialPiece() { return jogo.currentPlayerHasSpecialPiece(); }
    public boolean currentPlayerCanPlayMinigames() { return jogo.currentPlayerCanPlayMinigames(); }
    public boolean currentPlayerIsHuman() { return jogo.currentPlayerIsHuman(); }

    public int getNumberSpecialPiecesP1() { return jogo.getNumberSpecialPiecesP1(); }
    public int getNumberSpecialPiecesP2() { return jogo.getNumberSpecialPiecesP2(); }

    public void giveCurrentPlayerSpecialPiece() { jogo.giveCurrentPlayerSpecialPiece(); }

    public int getUndoCreditsFromCurrentPlayer() { return jogo.getUndoCreditsFromCurrentPlayer(); }
    public void removeUndoCreditsFromCurrentPlayer(int nrUndos) { jogo.removeUndoCreditsFromCurrentPlayer(nrUndos);}

    public boolean p1IsHuman() { return jogo.p1IsHuman(); }
    public boolean p2IsHuman() { return jogo.p2IsHuman(); }

    public void resetCurrentPlayerRound() { jogo.resetRoundForCurrentPlayer(); }

    // P1 & P2 methods

    public String getP1Name() { return jogo.getP1Name(); }
    public String getP2Name() { return jogo.getP2Name(); }

    public int getUndoCreditsP1() { return jogo.getUndoCreditsP1(); }
    public int getUndoCreditsP2() { return jogo.getUndoCreditsP2(); }

    // Misc. getters

    public String getEndGameInfo() { return jogo.getEndGameInfo(); }
    public String getBoardAsString() { return jogo.getBoardAsString(); }
    public TypePiece[][] getCopyOfBoard() { return jogo.getCopyOfBoard(); }
    public int getNumberBoardColumns() { return jogo.getNumberBoardColumns(); }
    public int getNumberBoardRows() { return jogo.getNumberBoardRows(); }
    public int getMoveFromAI() { return jogo.getMoveFromAI(); }

    public JogoStates getState() { return jogo.getState(); }

    // Minigame

    public void prepareMinigame() { jogo.prepareMinigame(); }
    public void setMinigameAnswer(String answer) { jogo.setMinigameAnswer(answer); }

    public String getMinigameWording() { return jogo.getMinigameWording(); }
    public String getMinigameQuestion() { return jogo.getMinigameQuestion(); }

    public boolean isMinigameFinished() { return jogo.isMinigameFinished(); }
    public boolean hasWonMinigame() { return jogo.hasWonMinigame(); }

    //
    // Replay Logic
    //

    public void prepareForReplay() {
        jogo.startJogo();
        jogo.setCurrentPlayer(firstPlayer);
    }

    public void replayMove() { commandManager.replayMove(); }

    public boolean hasCommandForReplay() { return commandManager.hasCommandForReplay(); }
}
