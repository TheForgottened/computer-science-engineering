package pt.isec.angelopaiva.jogo.iu.gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import pt.isec.angelopaiva.jogo.iu.gui.estados.*;
import pt.isec.angelopaiva.jogo.iu.gui.resources.CSSManager;
import pt.isec.angelopaiva.jogo.iu.gui.resources.styles.ProjectStyles;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;

public class PrincipalPane extends BorderPane {
    private final JogoObservable jogoObservable;

    PrincipalPane(JogoObservable jogoObservable) {
        this.jogoObservable = jogoObservable;
        CSSManager.setCSS(this, ProjectStyles.STYLES);

        createView();
    }

    private void createView() {
        // Paineis dos Estados
        ChooseOptionPane chooseOptionPane = new ChooseOptionPane(jogoObservable);
        EndGamePane endGamePane = new EndGamePane(jogoObservable);
        GamePane gamePane = new GamePane(jogoObservable);
        MinigameForSpecialPiecePane minigameForSpecialPiecePane = new MinigameForSpecialPiecePane(jogoObservable);
        WaitPlayersNameAndTypePane waitPlayersNameAndTypePane = new WaitPlayersNameAndTypePane(jogoObservable);
        WatchReplayPane watchReplayPane = new WatchReplayPane(jogoObservable);

        StackPane centerPane = new StackPane(
                chooseOptionPane, endGamePane, gamePane,
                minigameForSpecialPiecePane, waitPlayersNameAndTypePane, watchReplayPane
        );

        setCenter(centerPane);
    }
}
