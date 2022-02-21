package pt.isec.forgotten.balls.logic.data;

import pt.isec.forgotten.balls.logic.TypeBall;

import java.util.ArrayList;
import java.util.Collections;

public class BallsGame {
    public static final int NR_WHITE = 10;
    public static final int NR_BLACK  = 10;
    ArrayList<TypeBall> bag;
    int nrWhiteBallsWon, nrBlackBallsOut, nrWhiteBallsOut;
    int bet;

    public BallsGame() {
        startGame();
    }

    public void startGame() {
        this.bag = new ArrayList<>();
        this.bag.addAll(Collections.nCopies(NR_WHITE, TypeBall.White));
        this.bag.addAll(Collections.nCopies(NR_BLACK, TypeBall.Black));
        this.nrWhiteBallsWon = 0;
        this.nrBlackBallsOut = 0;
        this.nrWhiteBallsOut = 0;
        this.bet = 0;

        Collections.shuffle(this.bag);
    }

    public static int getNrWhite() { return NR_WHITE; }
    public static int getNrBlack() { return NR_BLACK; }
    public int getNrWhiteBallsWon() { return this.nrWhiteBallsWon; }
    public int getBet() { return bet; }

    public boolean isBagEmpty() { return this.bag.isEmpty(); }

    public boolean bet(int balls) {
        if (balls > this.nrWhiteBallsWon) {
            return false;
        }

        this.nrWhiteBallsWon -= balls;
        this.bet = balls;

        return true;
    }

    public void loseBet() {

    }

    public void recoverBet() {
        this.nrWhiteBallsWon += this.bet;
    }

    public void winWhite() {
        this.nrWhiteBallsWon++;
    }

    public boolean loseWhite() {
        if (this.nrWhiteBallsWon < 1) {
            return false;
        }

        this.nrWhiteBallsWon--;
        return true;
    }

    public void outBlack() { this.nrBlackBallsOut++; }
    public void outWhite() { this.nrWhiteBallsOut++; }

    public void returnBalls(int nrBalls, TypeBall type) {
        if (nrBalls > 0) {
            this.bag.addAll(Collections.nCopies(nrBalls, type));
        }

        Collections.shuffle(this.bag);
    }

    public TypeBall takeBallFromBag() {
        if (this.bag.isEmpty()) {
            return TypeBall.none;
        }

        return this.bag.remove(0);
    }

    @Override
    public String toString() {
        return "Jogo{" +
                "saco=" + this.bag +
                ", nrBolasBrancasGanhas=" + this.nrWhiteBallsWon +
                ", nrBolasPretasFora=" + this.nrBlackBallsOut +
                ", nrBolasBrancasFora=" + this.nrWhiteBallsOut +
                ", aposta=" + this.bet +
                '}';
    }
}

