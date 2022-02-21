package pt.isec.angelopaiva.jogo.logica.estados;

import pt.isec.angelopaiva.jogo.logica.AppStates;
import pt.isec.angelopaiva.jogo.logica.JogoManager;

public class ChooseOption extends StateAdapter {
    public ChooseOption(JogoManager jogoManager) { super(jogoManager); }

    @Override
    public IState startWaitPlayersNameAndType() { return new WaitPlayersNameAndType(jogoManager); }

    @Override
    public IState startWatchReplay() { return new WatchReplay(jogoManager); }

    @Override
    public IState startGame() { return new Game(jogoManager); }

    @Override
    public AppStates getState() { return AppStates.CHOOSE_OPTION; }
}
