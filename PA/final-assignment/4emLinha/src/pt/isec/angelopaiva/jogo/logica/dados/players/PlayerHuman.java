package pt.isec.angelopaiva.jogo.logica.dados.players;

public class PlayerHuman extends Player {
    private int round;

    public PlayerHuman(String name) {
        super(name);
        super.undoCredits = 5;
        round = 1;
    }

    @Override
    public int getRound() { return round; }

    @Override
    public void updateRound() { round++; }

    @Override
    public void resetRound() { round = 1; }

    @Override
    public boolean isHuman() { return true; }

    @Override
    public int getRandomMove(int minCol, int maxCol) { return -1; }
}
