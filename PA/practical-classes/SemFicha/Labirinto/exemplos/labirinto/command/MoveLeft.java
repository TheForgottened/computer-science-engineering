package exemplos.labirinto.command;

import exemplos.labirinto.modelo.LabirintoGame;

public class MoveLeft extends CommandAdapter {
    public MoveLeft(LabirintoGame receiver) {
        super(receiver);
    }

    @Override
    public boolean execute() {
        return this.receiver.left();
    }

    @Override
    public boolean undo() {
        return this.receiver.right();
    }
}
