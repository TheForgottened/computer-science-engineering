package pt.isec.forgotten.balls.logic.states;

import pt.isec.forgotten.balls.logic.Situation;
import pt.isec.forgotten.balls.logic.data.BallsGame;

public class End extends StateAdapter {
    public End(BallsGame game) { super(game); }

    public Situation getSituation() {
        return Situation.End;
    }
}
