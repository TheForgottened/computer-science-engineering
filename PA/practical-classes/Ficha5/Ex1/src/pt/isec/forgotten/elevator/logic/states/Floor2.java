package pt.isec.forgotten.elevator.logic.states;

import pt.isec.forgotten.elevator.logic.Situation;
import pt.isec.forgotten.elevator.logic.states.Floor1;

public class Floor2 extends StateAdapter {
    @Override
    public IState down() {
        return new Floor1();
    }

    public Situation getSituation() {
        return Situation.Floor2;
    }
}
