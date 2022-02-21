package pt.isec.angelopaiva.jogo.logica.dados.players;

import java.io.Serializable;

public abstract class Player implements Serializable {
    private final String name;
    protected int undoCredits;
    protected int specialPiece;

    protected Player(String name) {
        this.name = name;
        this.undoCredits = 0;
        this.specialPiece = 0;
    }

    public String getName() { return name; }

    public int getUndoCredits() { return undoCredits; }
    public void removeUndoCredits(int nr) { undoCredits -= nr; }

    public int getSpecialPiece() { return specialPiece; }
    public void addSpecialPiece() { specialPiece++; }
    public void removeSpecialPiece() { specialPiece--; }

    public abstract int getRound();
    public abstract void updateRound();
    public abstract void resetRound();

    public abstract boolean isHuman();
    public abstract int getRandomMove(int minCol, int maxCol);
}
