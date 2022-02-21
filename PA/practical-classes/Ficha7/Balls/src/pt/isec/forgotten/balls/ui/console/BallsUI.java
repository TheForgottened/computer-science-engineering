package pt.isec.forgotten.balls.ui.console;

import pt.isec.forgotten.balls.logic.StateMachine;
import pt.isec.forgotten.balls.utils.UtilsUI;

public class BallsUI {
    StateMachine sm;
    Boolean exit;

    public BallsUI(StateMachine sm) {
        this.sm = sm;
    }

    public void start() {
        exit = false;

        while (!exit) {
            switch (this.sm.getSituation()) {
                case Start:
                    startUI();
                    break;

                case Phase1:
                    phase1UI();
                    break;

                case Phase2:
                    phase2UI();
                    break;

                case End:
                    endUI();
                    break;
            }
        }
    }

    private void startUI() {
        int op = UtilsUI.chooseOption("Start", "Exit");

        switch (op) {
            case 1:
                sm.start();
                break;

            case 2:
                sm.finish();
        }
    }

    private void phase1UI() {
        switch (UtilsUI.chooseOption("Bet", "End", "Exit")) {
            case 1:
                int bet;

                do {
                    bet = UtilsUI.askInt("Number of white balls you want to bet [0 - " + sm.getNrWhiteBallsWon() + "]: ");
                } while (bet < 0 || bet > sm.getNrWhiteBallsWon());

                sm.bet(bet);
                break;

            case 2:
                sm.finish();
                break;

            default:
                exit = true;
                break;
        }
    }

    private void phase2UI() {
        System.out.println("A black ball was drawn. You lost the bet.");
        switch (UtilsUI.chooseOption("Lose white", "Remove 2 balls from bag", "Exit")) {
            case 1 -> sm.removeWhiteFromPile();
            case 2 -> sm.take2FromBag();
            default -> exit = true;
        }
    }

    private void endUI() {
        System.out.println("The game ended.");
        System.out.println("You won " + sm.getNrWhiteBallsWon() + " white balls!\n\n");

        switch (UtilsUI.chooseOption("Start", "Exit")) {
            case 1 -> sm.start();
            default -> exit = true;
        }
    }
}
