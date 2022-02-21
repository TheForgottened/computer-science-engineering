package pt.isec.forgotten.balls.logic.states;

import pt.isec.forgotten.balls.logic.Situation;
import pt.isec.forgotten.balls.logic.TypeBall;
import pt.isec.forgotten.balls.logic.data.BallsGame;

import java.lang.reflect.Type;

public class Phase2 extends StateAdapter {
    public Phase2(BallsGame game) { super(game); }

    public Situation getSituation() {
        return Situation.Phase2;
    }

    @Override
    public IState take2FromBag() {
        for (int i = 0; i < 2; i++) {
            TypeBall b = game.takeBallFromBag();

            if (b == TypeBall.Black) {
                game.returnBalls(1, TypeBall.Black);
            } else if (b == TypeBall.White) {
                game.outWhite();
            } else {
                return new End(game);
            }

            if (game.isBagEmpty()) {
                return new End(game);
            }
        }

        return new Phase1(game);
    }

    @Override
    public IState removeWhiteFromPile() {
        if (!game.loseWhite()) {
            return this;
        }

        game.loseWhite();
        return new Phase1(game);
    }
}
