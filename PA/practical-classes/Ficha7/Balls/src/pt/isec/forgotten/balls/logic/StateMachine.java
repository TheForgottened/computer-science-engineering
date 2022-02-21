package pt.isec.forgotten.balls.logic;

import pt.isec.forgotten.balls.logic.data.BallsGame;
import pt.isec.forgotten.balls.logic.states.IState;
import pt.isec.forgotten.balls.logic.states.Start;

public class StateMachine {
    IState current;
    BallsGame game;

    public StateMachine() {
        this.game = new BallsGame();
        this.current = new Start(this.game);
    }

    public void start() {
        this.current = this.current.start();
    }

    public void bet(int nBalls) { current = current.bet(nBalls); }
    public void take2FromBag() { current = current.take2FromBag(); }
    public void removeWhiteFromPile() { current = current.removeWhiteFromPile(); }
    public void finish() { current = current.finish(); }

    public int getNrWhiteBallsWon() { return game.getNrWhiteBallsWon(); }

    public String getSituationGame() { return "IDK"; }

    public Situation getSituation() {
        return this.current.getSituation();
    }
}
