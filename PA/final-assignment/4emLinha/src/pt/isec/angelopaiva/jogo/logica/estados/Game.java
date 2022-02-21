package pt.isec.angelopaiva.jogo.logica.estados;

import pt.isec.angelopaiva.jogo.logica.*;

public class Game extends StateAdapter {
    protected Game(JogoManager jogoManager) { super(jogoManager); }

    @Override
    public IState placePieceOnColumn(int column, boolean useSpecialPiece) {
        if (useSpecialPiece) {
            jogoManager.placeSpecialPieceOnColumn(column);
            return new Game(jogoManager);
        }

        if (!jogoManager.canPlacePieceOnColumn(column)) return this;

        jogoManager.placePieceOnColumn(column);

        if (jogoManager.getState() != JogoStates.UNFINISHED) return new EndGame(jogoManager, true);

        return new Game(jogoManager);
    }

    @Override
    public IState placePieceOnColumnAI() {
        int column;

        do {
            column = jogoManager.getMoveFromAI();
        } while (!jogoManager.canPlacePieceOnColumn(column));

        jogoManager.placePieceOnColumn(column);

        if (jogoManager.getState() != JogoStates.UNFINISHED) return new EndGame(jogoManager, true);

        return new Game(jogoManager);
    }

    @Override
    public IState undoMoves(int nrUndos) {
        if (nrUndos <= 0) return this;
        if (nrUndos > jogoManager.getMaxUndos()) return this;

        jogoManager.removeUndoCreditsFromCurrentPlayer(nrUndos);

        for (int i = 0; i < nrUndos; i++) jogoManager.undo();

        return new Game(jogoManager);
    }

    @Override
    public IState startMinigameForSpecialPiece() {
        if (jogoManager.currentPlayerCanPlayMinigames()) return new MinigameForSpecialPiece(jogoManager);

        return this;
    }

    @Override
    public IState goBack() { return new ChooseOption(jogoManager); }

    @Override
    public AppStates getState() { return AppStates.GAME; }
}
