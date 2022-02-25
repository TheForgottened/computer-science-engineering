package pt.isec.metapd;

import pt.isec.metapd.logic.MetaPdSm;
import pt.isec.metapd.ui.gui.GraphicalUI;
import pt.isec.metapd.ui.tui.TextUI;

import java.io.IOException;

public class Client {
    private static final boolean USE_GUI = true;

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Invalid number of arguments! Usage: <lb address> <lb port>");
            return;
        }

        MetaPdSm metaPdSM = new MetaPdSm(args[0], Integer.parseInt(args[1]));

        if (USE_GUI) {
            GraphicalUI gui = new GraphicalUI(metaPdSM);
            gui.run();
        } else {
            TextUI tui = new TextUI(metaPdSM);
            tui.run();
        }
    }
}
