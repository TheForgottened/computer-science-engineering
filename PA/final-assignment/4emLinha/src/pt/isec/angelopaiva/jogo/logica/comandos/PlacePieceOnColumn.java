package pt.isec.angelopaiva.jogo.logica.comandos;

import pt.isec.angelopaiva.jogo.logica.dados.Jogo;

public class PlacePieceOnColumn extends CommandAdapter {
    private final int column;

    public PlacePieceOnColumn(Jogo receiver, int column) {
        super(receiver);
        this.column = column;
    }

    @Override
    public boolean execute() {
       receiver.placePieceOnColumn(column);
       return true;
    }

    @Override
    public boolean undo() {
        receiver.removePieceFromColumn(column);
        return true;
    }
}
