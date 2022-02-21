package pt.isec.angelopaiva.jogo.logica;

import pt.isec.angelopaiva.jogo.logica.comandos.ICommand;
import pt.isec.angelopaiva.jogo.logica.comandos.UpdateJogo;

import java.io.Serializable;
import java.util.ArrayList;

public class CommandManager implements Serializable {
    private final ArrayList<ICommand> history = new ArrayList<>();

    // Invokes a command and manages the history
    public void invokeCommand(ICommand command) {
        if (command.execute()) {
            history.add(command);
            return;
        }

        history.clear();
    }

    public boolean undo() {
        if (history.isEmpty()) return false;

        ICommand command;

        do {
            command = history.remove(history.size() - 1);
            command.undo();
        } while (!history.isEmpty() && command instanceof UpdateJogo);

        return true;
    }

    public boolean hasCommandForReplay() {
        int counter = 0;

        for (ICommand replay : history) if (!(replay instanceof UpdateJogo)) counter++;

        return counter > 0;
    }

    public int getNrCommandsForReplay() {
        int counter = 0;

        for (ICommand replay : history) if (!(replay instanceof UpdateJogo)) counter++;

        return counter;
    }

    public void replayMove() {
        if (history.isEmpty()) return;

        ICommand command;

        do {
            command = history.remove(0);
            command.execute();
        } while (!history.isEmpty() && (!(command instanceof UpdateJogo) || (history.get(0) instanceof UpdateJogo)));
        // In order to visualize a move, we gotta invoke one command and the UpdateJogo after
    }
}
