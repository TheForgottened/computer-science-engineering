package exemplos.labirinto.command;

import exemplos.labirinto.modelo.LabirintoGame;

public class MoveRight extends CommandAdapter {
    public MoveRight(LabirintoGame receiver) {
        super(receiver);
    }

    @Override
    public boolean execute() {
        return this.receiver.right();
    }

    @Override
    public boolean undo() {
        return this.receiver.left();
    }
}
