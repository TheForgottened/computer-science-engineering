package exemplos.labirinto.command;

import java.util.ArrayList;

public class CommandManager {
    private ArrayList<ICommand> history = new ArrayList<ICommand>();
    private ArrayList<ICommand> redoList = new ArrayList<ICommand>();

    public void invokeCommand(ICommand command) {
        if (command.execute()) {
            // Se o comando foi executado com sucesso
            // E se tem undo correspondente
            history.add(command);
        } else {
            // Não tem undo
            // Esvazia-se o histórico
            history.clear();
        }
        // Depois de se executar um comando
        // Esvazia-se o redo
        redoList.clear();
    }

    public void undo() {
        if (history.size() > 0) {
            // Se o histórico não esta vazio
            ICommand commandUndo = history.remove(history.size() - 1);
            commandUndo.undo();
            redoList.add(commandUndo);
        }
    }

    public void redo() {
        if (redoList.size() > 0) {
            // se a lista de comandos redo nao esta vazia
            ICommand commandRedo = redoList.remove(redoList.size() - 1);
            commandRedo.execute();
            history.add(commandRedo);
        }
    }

    public boolean isHistoryEmpty() {
        return history.isEmpty();
    }

    public boolean isRedoVazio() {
        return redoList.isEmpty();
    }

    @Override
    public String toString() {
        return "\nhistorico=" + history + "\nredoLista=" + redoList;
    }
}
