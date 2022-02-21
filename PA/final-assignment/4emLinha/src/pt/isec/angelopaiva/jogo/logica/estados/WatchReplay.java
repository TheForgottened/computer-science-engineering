package pt.isec.angelopaiva.jogo.logica.estados;

import pt.isec.angelopaiva.jogo.logica.AppStates;
import pt.isec.angelopaiva.jogo.logica.JogoManager;

public class WatchReplay extends StateAdapter {
    protected WatchReplay(JogoManager jogoManager) {
        super(jogoManager);
        jogoManager.prepareForReplay();
    }

    @Override
    public IState replayMove() {
        jogoManager.replayMove();

        if (!jogoManager.hasCommandForReplay()) return new EndGame(jogoManager, false);

        return this;
    }

    @Override
    public AppStates getState() { return AppStates.WATCH_REPLAY; }
}
