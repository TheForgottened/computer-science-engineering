package exemplos.labirinto.command;

import exemplos.labirinto.modelo.LabirintoGame;

public abstract class CommandAdapter implements ICommand {
    protected final LabirintoGame receiver;

    protected CommandAdapter(LabirintoGame receiver){
        this.receiver = receiver;
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public boolean undo() {
        return false;
    }
}
