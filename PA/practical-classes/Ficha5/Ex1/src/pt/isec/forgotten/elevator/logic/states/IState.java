package pt.isec.forgotten.elevator.logic.states;

import pt.isec.forgotten.elevator.logic.Situation;

public interface IState {
    public IState up();

    public IState down();

    public Situation getSituation();
}
