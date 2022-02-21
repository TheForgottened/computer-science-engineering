package pt.isec.forgotten.balls;

import pt.isec.forgotten.balls.logic.StateMachine;
import pt.isec.forgotten.balls.ui.console.BallsUI;

public class BallsApp {
    public static void main(String[] args) {
        StateMachine sm = new StateMachine();
        BallsUI ballsUI = new BallsUI(sm);

        ballsUI.start();
    }
}
