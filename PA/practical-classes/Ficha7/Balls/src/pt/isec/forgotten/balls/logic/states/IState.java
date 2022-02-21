package pt.isec.forgotten.balls.logic.states;

import pt.isec.forgotten.balls.logic.Situation;

public interface IState {
    IState start();
    IState bet(int nrBalls);
    IState take2FromBag();
    IState removeWhiteFromPile();
    IState finish();

    Situation getSituation();
}

