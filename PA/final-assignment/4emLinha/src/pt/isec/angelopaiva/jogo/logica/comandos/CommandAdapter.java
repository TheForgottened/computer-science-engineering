package pt.isec.angelopaiva.jogo.logica.comandos;

import pt.isec.angelopaiva.jogo.logica.dados.Jogo;

import java.io.Serializable;

public abstract class CommandAdapter implements ICommand, Serializable {
    protected final Jogo receiver;

    protected CommandAdapter(Jogo receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean execute() { return false; }

    @Override
    public boolean undo() { return false; }
}
