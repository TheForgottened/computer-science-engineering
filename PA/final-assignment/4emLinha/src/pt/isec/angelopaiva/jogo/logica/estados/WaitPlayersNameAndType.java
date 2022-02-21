package pt.isec.angelopaiva.jogo.logica.estados;

import pt.isec.angelopaiva.jogo.logica.AppStates;
import pt.isec.angelopaiva.jogo.logica.JogoManager;
import pt.isec.angelopaiva.jogo.logica.dados.players.Player;

public class WaitPlayersNameAndType extends StateAdapter {
    protected WaitPlayersNameAndType(JogoManager jogoManager) { super(jogoManager); }

    @Override
    public IState setPlayers(Player p1, Player p2) {
        if (p1 == null || p2 == null) return this;
        if (p1.getName().isBlank() || p2.getName().isBlank()) return this;
        if (p1.getName().equalsIgnoreCase(p2.getName())) return this;

        jogoManager.startJogo();
        jogoManager.setPlayers(p1, p2);
        return new Game(jogoManager);
    }

    @Override
    public IState goBack() { return new ChooseOption(jogoManager); }

    @Override
    public AppStates getState() { return AppStates.WAIT_PLAYERS_NAME_AND_TYPE; }
}
