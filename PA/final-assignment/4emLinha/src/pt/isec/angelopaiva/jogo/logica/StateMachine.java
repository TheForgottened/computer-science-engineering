package pt.isec.angelopaiva.jogo.logica;

import pt.isec.angelopaiva.jogo.logica.dados.players.Player;
import pt.isec.angelopaiva.jogo.logica.estados.ChooseOption;
import pt.isec.angelopaiva.jogo.logica.estados.IState;
import pt.isec.angelopaiva.jogo.utils.UtilsFiles;

import java.io.File;

public class StateMachine {
    private IState current;
    private JogoManager jogoManager;

    public StateMachine() {
        this.jogoManager = new JogoManager();
        this.current = new ChooseOption(jogoManager);
    }

    //
    // Depend on state
    //

    public void startWaitPlayersNameAndType() { current = current.startWaitPlayersNameAndType(); }
    public void startMinigameForSpecialPiece() { current = current.startMinigameForSpecialPiece(); }

    public void goBack() { current = current.goBack(); }

    public void setPlayers(Player p1, Player p2) { current = current.setPlayers(p1, p2); }

    public boolean placePieceOnColumn(int column, boolean useSpecialPiece) {
        boolean b = jogoManager.canPlacePieceOnColumn(column);

        current = current.placePieceOnColumn(column, useSpecialPiece);

        if (useSpecialPiece) return true;

        return b;
    }

    public void placePieceOnColumnAI() { current = current.placePieceOnColumnAI(); }

    public void setMinigameAnswer(String answer) { current = current.setMinigameAnswer(answer); }

    public void undoMoves(int nrMoves) { current = current.undoMoves(nrMoves); }

    public void replayMove() { current = current.replayMove(); }

    public AppStates getCurrentState() { return current.getState(); }

    // Load Logic

    public boolean loadGameForReplay(File file) {
        JogoManager replayJogoManager = UtilsFiles.loadJogoManagerFromFile(file);

        if (replayJogoManager == null) return false;

        updateJogoManager(replayJogoManager);

        current = current.startWatchReplay();

        return true;
    }

    public boolean loadGameForReplay(String fileName) {
        JogoManager replayJogoManager = UtilsFiles.loadReplayFromFile(fileName);

        if (replayJogoManager == null) return false;

        updateJogoManager(replayJogoManager);

        current = current.startWatchReplay();

        return true;
    }

    public boolean loadGame(String fileName) {
        JogoManager loadedJogoManager = UtilsFiles.loadSaveFromFile(fileName);

        if (loadedJogoManager == null) return false;

        updateJogoManager(loadedJogoManager);

        current = current.startGame();

        return true;
    }

    public boolean loadGame(File file) {
        JogoManager loadedJogoManager = UtilsFiles.loadJogoManagerFromFile(file);

        if (loadedJogoManager == null) return false;

        updateJogoManager(loadedJogoManager);

        current = current.startGame();

        return true;
    }


    private void updateJogoManager(JogoManager newJogoManager) {
        this.jogoManager = newJogoManager;
        current.setJogoManager(newJogoManager);
    }

    //
    // Don't depend on state
    //

    // Save Logic

    public void saveGameForSave(String saveName) { UtilsFiles.saveGameToSaveFile(jogoManager, saveName); }
    public void saveGameForSave(File file) { UtilsFiles.saveJogoManagerToFile(jogoManager, file); }

    // Current Player Methods

    public int getCurrentPlayer() { return jogoManager.getCurrentPlayer(); }

    public String getCurrentPlayerName() { return jogoManager.getCurrentPlayerName(); }
    public boolean currentPlayerIsHuman() { return jogoManager.currentPlayerIsHuman(); }
    public boolean currentPlayerHasSpecialPiece() { return jogoManager.currentPlayerHasSpecialPiece(); }
    public boolean currentPlayerCanPlayMinigames() { return jogoManager.currentPlayerCanPlayMinigames(); }
    public int getUndoCreditsFromCurrentPlayer() { return jogoManager.getUndoCreditsFromCurrentPlayer(); }

    // Player Specific Methods

    public String getP1Name() { return jogoManager.getP1Name(); }
    public boolean p1IsHuman() { return jogoManager.p1IsHuman(); }
    public int getNumberSpecialPiecesP1() { return jogoManager.getNumberSpecialPiecesP1(); }
    public int getUndoCreditsP1() { return jogoManager.getUndoCreditsP1(); }

    public String getP2Name() { return jogoManager.getP2Name(); }
    public boolean p2IsHuman() { return jogoManager.p2IsHuman(); }
    public int getNumberSpecialPiecesP2() { return jogoManager.getNumberSpecialPiecesP2(); }
    public int getUndoCreditsP2() { return jogoManager.getUndoCreditsP2(); }

    // Board Methods

    public int getNumberBoardColumns() { return jogoManager.getNumberBoardColumns(); }
    public int getNumberBoardRows() { return jogoManager.getNumberBoardRows(); }
    public String getBoardAsString() { return jogoManager.getBoardAsString(); }
    public TypePiece[][] getCopyOfBoard() { return jogoManager.getCopyOfBoard(); }

    // Get EndGame Info

    public String getEndGameInfo() { return jogoManager.getEndGameInfo(); }

    // Get Jogo State

    public JogoStates getJogoState() { return jogoManager.getState(); }

    // Minigame Methods

    public String getMinigameWording() { return jogoManager.getMinigameWording(); }
    public String getMinigameQuestion() { return jogoManager.getMinigameQuestion(); }
}
