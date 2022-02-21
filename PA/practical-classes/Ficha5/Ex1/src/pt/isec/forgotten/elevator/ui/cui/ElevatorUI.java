package pt.isec.forgotten.elevator.ui.cui;

import pt.isec.forgotten.elevator.logic.StateMachine;
import pt.isec.forgotten.elevator.utils.Utils;

public class ElevatorUI {
    StateMachine stateMachine;
    boolean leave;

    public ElevatorUI (StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public void start() {
        while (!leave) {
            switch (stateMachine.getCurrentSituation()) {
                case Floor0:
                    uiFloor0();
                    break;

                case Floor1:
                    uiFloor1();
                    break;

                case Floor2:
                    uiFloor2();
                    break;
            }
        }
    }

    private void uiFloor0() {
        System.out.println("Floor 0:");

        switch (Utils.chooseOption("Up", "Leave")) {
            case 1:
                stateMachine.up();
                break;

            default:
                this.leave = true;
                break;
        }
    }

    private void uiFloor1() {
        System.out.println("Floor 1:");

        switch (Utils.chooseOption("Up", "Down", "Leave")) {
            case 1:
                stateMachine.up();
                break;

            case 2:
                stateMachine.down();
                break;

            default:
                this.leave = true;
                break;
        }
    }

    private void uiFloor2() {
        System.out.println("Floor 2:");

        switch (Utils.chooseOption("Down", "Leave")) {
            case 1:
                stateMachine.down();
                break;

            default:
                this.leave = true;
                break;
        }
    }
}
