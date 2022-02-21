package pt.isec.forgotten.elevator.logic;

import pt.isec.forgotten.elevator.logic.states.Floor0;
import pt.isec.forgotten.elevator.logic.states.IState;

public class StateMachine {
    IState current;

    public StateMachine() {
        current = new Floor0();
    }

    public void up() {
        current = current.up();
    }

    public void down() {
        current = current.down();
    }

    public Situation getCurrentSituation() {
        return current.getSituation();
    }
}
