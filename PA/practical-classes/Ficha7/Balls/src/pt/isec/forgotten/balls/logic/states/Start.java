package pt.isec.forgotten.balls.logic.states;

import pt.isec.forgotten.balls.logic.Situation;
import pt.isec.forgotten.balls.logic.data.BallsGame;

public class Start extends StateAdapter {
    public Start(BallsGame game) { super(game); }

    @Override
    public IState start() {
        game.startGame();
        return new Phase1(game);
    }

    public Situation getSituation() {
        return Situation.Start;
    }
}
