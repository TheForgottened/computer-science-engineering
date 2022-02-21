package pt.isec.forgotten.elevator;

import pt.isec.forgotten.elevator.logic.StateMachine;
import pt.isec.forgotten.elevator.ui.cui.ElevatorUI;

public class ElevatorApp {
    public static void main(String[] args) {
        StateMachine stateMachine = new StateMachine();
        ElevatorUI elevatorUI = new ElevatorUI(stateMachine);

        elevatorUI.start();
    }
}
