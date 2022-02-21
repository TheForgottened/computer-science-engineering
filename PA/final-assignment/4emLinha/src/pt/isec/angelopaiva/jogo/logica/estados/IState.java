package pt.isec.angelopaiva.jogo.logica.estados;

import pt.isec.angelopaiva.jogo.logica.*;
import pt.isec.angelopaiva.jogo.logica.dados.players.Player;

public interface IState {
    void setJogoManager(JogoManager jogoManager);

    IState startWaitPlayersNameAndType();
    IState startGame();
    IState startMinigameForSpecialPiece();
    IState startWatchReplay();

    IState goBack();

    IState setPlayers(Player p1, Player p2);

    IState placePieceOnColumn(int column, boolean useSpecialPiece);
    IState placePieceOnColumnAI();

    IState setMinigameAnswer(String answer);

    IState undoMoves(int nrMoves);

    IState replayMove();

    AppStates getState();
}
