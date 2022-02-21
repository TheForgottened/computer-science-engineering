package pt.isec.angelopaiva.jogo.logica.estados;

import pt.isec.angelopaiva.jogo.logica.AppStates;
import pt.isec.angelopaiva.jogo.logica.JogoManager;

public class MinigameForSpecialPiece extends StateAdapter {
    protected MinigameForSpecialPiece(JogoManager jogoManager) {
        super(jogoManager);
        jogoManager.prepareMinigame();
    }

    @Override
    public IState goBack() { return new Game(jogoManager); }

    @Override
    public IState setMinigameAnswer(String answer) {
        jogoManager.setMinigameAnswer(answer);

        if (!jogoManager.isMinigameFinished()) return this;

        jogoManager.resetCurrentPlayerRound();

        if (jogoManager.hasWonMinigame()) {
            jogoManager.giveCurrentPlayerSpecialPiece();

            return new Game(jogoManager);
        }

        jogoManager.updateJogo();

        return new Game(jogoManager);
    }

    @Override
    public AppStates getState() { return AppStates.MINIGAME_FOR_SPECIAL_PIECE; }
}