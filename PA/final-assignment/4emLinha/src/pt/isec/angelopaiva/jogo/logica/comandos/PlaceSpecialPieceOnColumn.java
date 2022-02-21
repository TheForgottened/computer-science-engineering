package pt.isec.angelopaiva.jogo.logica.comandos;

import pt.isec.angelopaiva.jogo.logica.TypePiece;
import pt.isec.angelopaiva.jogo.logica.dados.Jogo;

import java.util.ArrayList;
import java.util.List;

public class PlaceSpecialPieceOnColumn extends CommandAdapter {
    private final int column;
    private List<TypePiece> oldColumn;

    public PlaceSpecialPieceOnColumn(Jogo receiver, int column) {
        super(receiver);
        this.column = column;
        oldColumn = new ArrayList<>();
    }

    @Override
    public boolean execute() {
        oldColumn = receiver.getColumnAsList(column);
        receiver.placeSpecialPieceOnColumn(column);
        return true;
    }

    @Override
    public boolean undo() {
        receiver.replaceColumnWithList(oldColumn, column);
        return true;
    }
}
