package exemplos.labirinto.command;

import exemplos.labirinto.modelo.LabirintoGame;

public class MoveUp extends CommandAdapter {
    public MoveUp(LabirintoGame receiver) {
        super(receiver);
    }

    @Override
    public boolean execute() {
        return this.receiver.up();
    }

    @Override
    public boolean undo() {
        return this.receiver.down();
    }
}
