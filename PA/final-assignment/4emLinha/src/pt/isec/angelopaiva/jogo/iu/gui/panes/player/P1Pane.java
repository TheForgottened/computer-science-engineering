package pt.isec.angelopaiva.jogo.iu.gui.panes.player;

import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;

public class P1Pane extends PlayerPane {
    public P1Pane(JogoObservable jogoObservable) { super(jogoObservable); }

    @Override
    protected void update(boolean isReplay) {
        name.setText(jogoObservable.getP1Name());

        switch (jogoObservable.getCurrentPlayer()) {
            case 1 -> name.setFill(DraculaTheme.GREEN);
            case 2 -> name.setFill(DraculaTheme.RED);
            default -> name.setFill(DraculaTheme.FOREGROUND);
        }

        if (isReplay) {
            info.setText("Cor da Peça: Vermelho"
                + "\nTipo: " + (jogoObservable.p1IsHuman() ? "Humano" : "CPU")
            );

            return;
        }

        info.setText("Cor da Peça: Vermelho"
            + "\nTipo: " + (jogoObservable.p1IsHuman() ? "Humano" : "CPU")
            + "\nCréditos: " + jogoObservable.getUndoCreditsP1()
            + "\nPeças Especiais: " + jogoObservable.getNumberSpecialPiecesP1()
        );
    }
}
