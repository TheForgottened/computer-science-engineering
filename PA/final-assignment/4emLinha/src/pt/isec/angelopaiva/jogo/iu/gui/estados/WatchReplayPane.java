package pt.isec.angelopaiva.jogo.iu.gui.estados;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import pt.isec.angelopaiva.jogo.iu.gui.controls.CustomButton;
import pt.isec.angelopaiva.jogo.iu.gui.panes.BoardPane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.ButtonHorizontalPane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.player.P1Pane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.player.P2Pane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.player.PlayerPane;
import pt.isec.angelopaiva.jogo.iu.gui.resources.SoundPlayer;
import pt.isec.angelopaiva.jogo.iu.gui.resources.sounds.ProjectSounds;
import pt.isec.angelopaiva.jogo.logica.AppStates;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;
import pt.isec.angelopaiva.jogo.logica.Property;

public class WatchReplayPane extends BorderPane {
    private final JogoObservable jogoObservable;

    private final CustomButton nextMoveBtn;

    private final PlayerPane p1Pane;
    private final PlayerPane p2Pane;

    private final BoardPane boardPane;

    public WatchReplayPane(JogoObservable jogoObservable) {
        this.jogoObservable = jogoObservable;

        nextMoveBtn = new CustomButton("Próxima Jogada");

        p1Pane = new P1Pane(jogoObservable);
        p2Pane = new P2Pane(jogoObservable);

        boardPane = new BoardPane(jogoObservable);

        createView();
        registerObserver();
        updateVisilibity();
    }

    private void updateVisilibity() { setVisible(jogoObservable.getCurrentState() == AppStates.WATCH_REPLAY); }

    private void registerObserver() {
        jogoObservable.addPropertyChangeListener(Property.PROPERTY_GENERAL.toString(), evt -> updateVisilibity());
    }

    private void createView() {
        ButtonHorizontalPane hBoxBoardBtn = new ButtonHorizontalPane();

        nextMoveBtn.setOnAction(e -> {
            jogoObservable.replayMove();
            SoundPlayer.playSound(ProjectSounds.PIECE_FALLING);
        });

        setOnKeyPressed(e -> {
            if (e.getCode() != KeyCode.ENTER) return;

            jogoObservable.replayMove();
            SoundPlayer.playSound(ProjectSounds.PIECE_FALLING);
        });

        hBoxBoardBtn.getChildren().addAll(nextMoveBtn);

        setLeft(p1Pane);
        setRight(p2Pane);
        setCenter(boardPane);
        setBottom(hBoxBoardBtn);
    }
}
