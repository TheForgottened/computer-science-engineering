package pt.isec.angelopaiva.jogo.logica.estados;

import pt.isec.angelopaiva.jogo.logica.JogoManager;
import pt.isec.angelopaiva.jogo.logica.dados.players.Player;

public abstract class StateAdapter implements IState {
    protected JogoManager jogoManager;

    protected StateAdapter(JogoManager jogoManager) { this.jogoManager = jogoManager; }

    @Override
    public void setJogoManager(JogoManager jogoManager) { this.jogoManager = jogoManager; }

    @Override
    public IState startWaitPlayersNameAndType() { return this; }
    @Override
    public IState startGame() { return this; }
    @Override
    public IState startMinigameForSpecialPiece() { return this; }
    @Override
    public IState startWatchReplay() { return this; }
    @Override
    public IState goBack() { return this; }

    @Override
    public IState setPlayers(Player p1, Player p2) { return this; }

    @Override
    public IState placePieceOnColumn(int column, boolean useSpecialPiece) { return this; }
    @Override
    public IState placePieceOnColumnAI() { return this; }

    @Override
    public IState setMinigameAnswer(String answer) { return this; }

    @Override
    public IState undoMoves(int nrMoves) { return this; }

    @Override
    public IState replayMove() { return this; }
}
