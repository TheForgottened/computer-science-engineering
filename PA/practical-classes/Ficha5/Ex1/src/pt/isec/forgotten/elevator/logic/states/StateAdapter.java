package pt.isec.forgotten.elevator.logic.states;

import pt.isec.forgotten.elevator.logic.states.IState;

public abstract class StateAdapter implements IState {
    @Override
    public IState up() {
        return this;
    }

    @Override
    public IState down() {
        return this;
    }
}
