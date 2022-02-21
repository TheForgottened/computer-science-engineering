package pt.isec.forgotten.balls.logic.states;

import pt.isec.forgotten.balls.logic.Situation;
import pt.isec.forgotten.balls.logic.TypeBall;
import pt.isec.forgotten.balls.logic.data.BallsGame;

import java.lang.reflect.Type;

public class Phase1 extends StateAdapter {
    public Phase1(BallsGame game) { super(game); }

    public Situation getSituation() {
        return Situation.Phase1;
    }

    public IState bet(int nrBalls) {
        if (!game.bet(nrBalls)) {
            return this;
        }

        TypeBall ball = game.takeBallFromBag();

        if (ball == TypeBall.Black) {
            game.loseBet();
            game.outBlack();

            return new Phase2(game);
        }

        game.winWhite();

        int nrWhite = 0;

        for (int i = 0; !game.isBagEmpty() && i < game.getBet(); i++) {
            TypeBall b = game.takeBallFromBag();

            if (b == TypeBall.White) {
                nrWhite++;
            } else {
                game.outBlack();
            }
        }

        game.returnBalls(nrWhite, TypeBall.White);
        game.recoverBet();

        if (game.isBagEmpty()) {
            return new End(game);
        }

        return new Phase2(game);
    }

    public IState end() { return new End(game); }
}
