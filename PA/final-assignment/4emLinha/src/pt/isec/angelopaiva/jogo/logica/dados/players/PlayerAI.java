package pt.isec.angelopaiva.jogo.logica.dados.players;

import java.util.concurrent.ThreadLocalRandom;

public class PlayerAI extends Player {
    public PlayerAI(String name) {
        super(name);
        super.undoCredits = 0;
    }

    @Override
    public int getRound() { return 0; }

    @Override
    public void updateRound() { /* AI doesn't need any round logic */ }

    @Override
    public void resetRound() { /* AI doesn't need any round logic */ }

    @Override
    public boolean isHuman() { return false; }

    @Override
    public int getRandomMove(int minCol, int maxCol) { return ThreadLocalRandom.current().nextInt(minCol, maxCol); }
}
