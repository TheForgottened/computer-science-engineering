package exemplos.labirinto.command;

import exemplos.labirinto.modelo.LabirintoGame;

public class MoveDown extends CommandAdapter {
    public MoveDown(LabirintoGame receiver) {
        super(receiver);
    }

    @Override
    public boolean execute() {
        return this.receiver.down();
    }

    @Override
    public boolean undo() {
        return this.receiver.up();
    }
}
