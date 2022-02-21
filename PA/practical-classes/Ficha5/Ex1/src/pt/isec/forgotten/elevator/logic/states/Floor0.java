package pt.isec.forgotten.elevator.logic.states;

import pt.isec.forgotten.elevator.logic.Situation;

public class Floor0 extends StateAdapter {
    @Override
    public IState up() {
        return new Floor1();
    }

    public Situation getSituation() {
        return Situation.Floor0;
    }
}
