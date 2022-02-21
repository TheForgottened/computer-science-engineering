package pt.isec.forgotten.balls.logic.states;

import pt.isec.forgotten.balls.logic.Situation;
import pt.isec.forgotten.balls.logic.data.BallsGame;

public abstract class StateAdapter implements IState {
    protected BallsGame game;

    public StateAdapter(BallsGame game) {
        this.game = game;
    }

    @Override
    public IState start() { return this; }

    @Override
    public IState bet(int nrBalls) { return this; }

    @Override
    public IState take2FromBag() { return this; }

    @Override
    public IState removeWhiteFromPile() { return this; }

    @Override
    public IState finish() { return this; }

    public abstract Situation getSituation();
}

