package pt.isec.angelopaiva.jogo.logica;

import pt.isec.angelopaiva.jogo.logica.dados.players.Player;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

public class JogoObservable {
    private final StateMachine sm;
    private final PropertyChangeSupport propertyChangeSupport;

    public JogoObservable(StateMachine sm) {
        this.sm = sm;
        propertyChangeSupport = new PropertyChangeSupport(sm);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public AppStates getCurrentState() { return sm.getCurrentState(); }

    //
    // Methods that fire
    //

    public void startWaitPlayersNameAndType() {
        sm.startWaitPlayersNameAndType();
        propertyChangeSupport.firePropertyChange(Property.PROPERTY_GENERAL.toString(), null, null);
    }

    public void startMinigameForSpecialPiece() {
        sm.startMinigameForSpecialPiece();

        propertyChangeSupport.firePropertyChange(Property.PROPERTY_GENERAL.toString(), null, null);
        propertyChangeSupport.firePropertyChange(Property.PROPERTY_MINIGAME.toString(), null, null);
    }

    public void goBack() {
        sm.goBack();
        propertyChangeSupport.firePropertyChange(Property.PROPERTY_GENERAL.toString(), null, null);
    }

    public void setPlayers(Player p1, Player p2) {
        sm.setPlayers(p1, p2);
        propertyChangeSupport.firePropertyChange(Property.PROPERTY_GENERAL.toString(), null, null);
        propertyChangeSupport.firePropertyChange(Property.PROPERTY_GAME.toString(), null, null);
    }

    public boolean placePieceOnColumn(int column, boolean useSpecialPiece) {
        boolean b = sm.placePieceOnColumn(column, useSpecialPiece);

        if (sm.getCurrentState() != AppStates.GAME) {
            propertyChangeSupport.firePropertyChange(Property.PROPERTY_GENERAL.toString(), null, null);
        }

        propertyChangeSupport.firePropertyChange(Property.PROPERTY_GAME.toString(), null, null);

        return b;
    }

    public void placePieceOnColumnAI() {
        sm.placePieceOnColumnAI();

        if (sm.getCurrentState() != AppStates.GAME) {
            propertyChangeSupport.firePropertyChange(Property.PROPERTY_GENERAL.toString(), null, null);
        }

        propertyChangeSupport.firePropertyChange(Property.PROPERTY_GAME.toString(), null, null);
    }

    public void setMinigameAnswer(String answer) {
        sm.setMinigameAnswer(answer);

        if (sm.getCurrentState() == AppStates.MINIGAME_FOR_SPECIAL_PIECE) {
            propertyChangeSupport.firePropertyChange(Property.PROPERTY_MINIGAME.toString(), null, null);
            return;
        }

        propertyChangeSupport.firePropertyChange(Property.PROPERTY_GENERAL.toString(), null, null);
        propertyChangeSupport.firePropertyChange(Property.PROPERTY_GAME.toString(), null, null);
    }

    public void undoMoves(int nrMoves) {
        sm.undoMoves(nrMoves);
        propertyChangeSupport.firePropertyChange(Property.PROPERTY_GAME.toString(), null, null);
    }

    public void replayMove() {
        sm.replayMove();

        propertyChangeSupport.firePropertyChange(Property.PROPERTY_REPLAY.toString(), null, null);

        if (sm.getCurrentState() != AppStates.WATCH_REPLAY) {
            propertyChangeSupport.firePropertyChange(Property.PROPERTY_GENERAL.toString(), null, null);
        }
    }

    public boolean loadGameForReplay(File file) {
        boolean success = sm.loadGameForReplay(file);

        if (success) {
            propertyChangeSupport.firePropertyChange(Property.PROPERTY_GENERAL.toString(), null, null);
            propertyChangeSupport.firePropertyChange(Property.PROPERTY_REPLAY.toString(), null, null);
        }

        return success;
    }


    public boolean loadGame(File file) {
        boolean success = sm.loadGame(file);

        if (success) {
            propertyChangeSupport.firePropertyChange(Property.PROPERTY_GENERAL.toString(), null, null);
            propertyChangeSupport.firePropertyChange(Property.PROPERTY_GAME.toString(), null, null);
        }

        return success;
    }

    //
    // Methods that don't fire
    //

    // Save Logic

    public void saveGameForSave(File file) { sm.saveGameForSave(file); }

    // Current Player Methods

    public int getCurrentPlayer() { return sm.getCurrentPlayer(); }

    public boolean currentPlayerIsHuman() { return sm.currentPlayerIsHuman(); }
    public boolean currentPlayerHasSpecialPiece() { return sm.currentPlayerHasSpecialPiece(); }
    public boolean currentPlayerCanPlayMinigames() { return sm.currentPlayerCanPlayMinigames(); }
    public int getUndoCreditsFromCurrentPlayer() { return sm.getUndoCreditsFromCurrentPlayer(); }

    // Player Specific Methods

    public String getP1Name() { return sm.getP1Name(); }
    public boolean p1IsHuman() { return sm.p1IsHuman(); }
    public int getNumberSpecialPiecesP1() { return sm.getNumberSpecialPiecesP1(); }
    public int getUndoCreditsP1() { return sm.getUndoCreditsP1(); }

    public String getP2Name() { return sm.getP2Name(); }
    public boolean p2IsHuman() { return sm.p2IsHuman(); }
    public int getNumberSpecialPiecesP2() { return sm.getNumberSpecialPiecesP2(); }
    public int getUndoCreditsP2() { return sm.getUndoCreditsP2(); }

    // Board Methods

    public int getNumberBoardColumns() { return sm.getNumberBoardColumns(); }
    public int getNumberBoardRows() { return sm.getNumberBoardRows(); }
    public TypePiece[][] getCopyOfBoard() { return sm.getCopyOfBoard(); }

    // Get Jogo State

    public JogoStates getJogoState() { return sm.getJogoState(); }

    // Minigame Methods

    public String getMinigameWording() { return sm.getMinigameWording(); }
    public String getMinigameQuestion() { return sm.getMinigameQuestion(); }
}
