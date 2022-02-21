package pt.isec.angelopaiva.jogo.iu.gui.panes.player;

import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;

public class P2Pane extends PlayerPane {
    public P2Pane(JogoObservable jogoObservable) { super(jogoObservable); }

    @Override
    protected void update(boolean isReplay) {
        name.setText(jogoObservable.getP2Name());

        switch (jogoObservable.getCurrentPlayer()) {
            case 1 -> name.setFill(DraculaTheme.RED);
            case 2 -> name.setFill(DraculaTheme.GREEN);
            default -> name.setFill(DraculaTheme.FOREGROUND);
        }

        if (isReplay) {
            info.setText("Cor da Peça: Roxo"
                + "\nTipo: " + (jogoObservable.p2IsHuman() ? "Humano" : "CPU")
            );

            return;
        }

        info.setText("Cor da Peça: Roxo"
            + "\nTipo: " + (jogoObservable.p2IsHuman() ? "Humano" : "CPU")
            + "\nCréditos: " + jogoObservable.getUndoCreditsP2()
            + "\nPeças Especiais: " + jogoObservable.getNumberSpecialPiecesP2()
        );
    }
}
