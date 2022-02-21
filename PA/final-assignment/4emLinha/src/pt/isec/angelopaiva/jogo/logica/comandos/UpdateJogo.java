package pt.isec.angelopaiva.jogo.logica.comandos;

import pt.isec.angelopaiva.jogo.logica.dados.Jogo;

public class UpdateJogo extends CommandAdapter {
    private int currentPlayer;

    public UpdateJogo(Jogo receiver) { super(receiver); }

    @Override
    public boolean execute() {
        currentPlayer = receiver.getCurrentPlayer();
        receiver.updateJogo();
        return true;
    }

    @Override
    public boolean undo() {
        receiver.setCurrentPlayer(currentPlayer);
        return true;
    }
}
