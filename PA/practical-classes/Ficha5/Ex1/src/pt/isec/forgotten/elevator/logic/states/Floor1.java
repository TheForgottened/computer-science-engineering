package pt.isec.forgotten.elevator.logic.states;

import pt.isec.forgotten.elevator.logic.Situation;
import pt.isec.forgotten.elevator.logic.states.Floor0;

public class Floor1 extends StateAdapter {
    @Override
    public IState up() {
        return new Floor2();
    }

    @Override
    public IState down() {
        return new Floor0();
    }

    public Situation getSituation() {
        return Situation.Floor1;
    }
}
