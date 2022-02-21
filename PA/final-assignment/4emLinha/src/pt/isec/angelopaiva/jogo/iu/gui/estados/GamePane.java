package pt.isec.angelopaiva.jogo.iu.gui.estados;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import pt.isec.angelopaiva.jogo.iu.gui.controls.CustomButton;
import pt.isec.angelopaiva.jogo.iu.gui.nodes.ErrorText;
import pt.isec.angelopaiva.jogo.iu.gui.panes.BoardPane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.ButtonHorizontalPane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.ErrorPane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.player.P1Pane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.player.P2Pane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.player.PlayerPane;
import pt.isec.angelopaiva.jogo.iu.gui.resources.SoundPlayer;
import pt.isec.angelopaiva.jogo.iu.gui.resources.sounds.ProjectSounds;
import pt.isec.angelopaiva.jogo.logica.AppStates;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;
import pt.isec.angelopaiva.jogo.logica.Property;
import pt.isec.angelopaiva.jogo.utils.UtilsFiles;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GamePane extends BorderPane {
    private final JogoObservable jogoObservable;

    private final ErrorText errorText;

    private final ComboBox<Integer> comboBoxNrUndos;
    private final CustomButton saveGameBtn;
    private final CustomButton undoBtn;
    private final CustomButton minigameBtn;
    private final CheckBox checkBoxSpecialPiece;
    private final CustomButton backToMainMenuBtn;

    private final PlayerPane p1Pane;
    private final PlayerPane p2Pane;

    private final BoardPane boardPane;

    public GamePane(JogoObservable jogoObservable) {
        this.jogoObservable = jogoObservable;

        errorText = new ErrorText();

        comboBoxNrUndos = new ComboBox<>();

        saveGameBtn = new CustomButton("Guardar Jogo");
        undoBtn = new CustomButton("Desfazer Jogadas");
        minigameBtn = new CustomButton("Jogar Minijogo");
        backToMainMenuBtn = new CustomButton("Menu Inicial");

        checkBoxSpecialPiece = new CheckBox("Usar peça especial");

        p1Pane = new P1Pane(jogoObservable);
        p2Pane = new P2Pane(jogoObservable);

        boardPane = new BoardPane(jogoObservable, checkBoxSpecialPiece, errorText);

        createView();
        registerObserver();
        updateVisilibity();
    }

    private void update() {
        if (!isVisible()) return;

        errorText.setText("");

        boolean currentPlayerIsHuman = jogoObservable.currentPlayerIsHuman();

        boardPane.setDisable(!currentPlayerIsHuman);
        checkBoxSpecialPiece.setSelected(false);
        disableButtons();

        comboBoxNrUndos.getItems().clear();
        for (int i = 0; i <= jogoObservable.getUndoCreditsFromCurrentPlayer(); i++) comboBoxNrUndos.getItems().add(i);
        comboBoxNrUndos.setValue(0);

        if (!currentPlayerIsHuman) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            scheduler.schedule(() -> {
                Platform.runLater(() -> {
                    jogoObservable.placePieceOnColumnAI();
                    SoundPlayer.playSound(ProjectSounds.PIECE_FALLING);
                });

                scheduler.shutdown();
            }, ThreadLocalRandom.current().nextInt(1, 4), TimeUnit.SECONDS);
        }
    }

    private void updateVisilibity() { setVisible(jogoObservable.getCurrentState() == AppStates.GAME); }

    private void registerObserver() {
        jogoObservable.addPropertyChangeListener(Property.PROPERTY_GAME.toString(), evt -> update());
        jogoObservable.addPropertyChangeListener(Property.PROPERTY_GENERAL.toString(), evt -> updateVisilibity());
    }

    private void createView() {
        ErrorPane errorPane = new ErrorPane();
        ButtonHorizontalPane hBoxBoardBtn = new ButtonHorizontalPane();

        saveGameBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(UtilsFiles.SAVES_PATH));
            fileChooser.setTitle("Guardar Jogo");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Ficheiros Save", "*.save"));

            File tempFile = fileChooser.showSaveDialog(getScene().getWindow());

            jogoObservable.saveGameForSave(tempFile);
        });

        undoBtn.setOnAction(e -> jogoObservable.undoMoves(comboBoxNrUndos.getValue()));
        minigameBtn.setOnAction(e -> jogoObservable.startMinigameForSpecialPiece());
        backToMainMenuBtn.setOnAction(e -> jogoObservable.goBack());

        hBoxBoardBtn.getChildren().addAll(
            saveGameBtn,
            undoBtn,
            comboBoxNrUndos,
            checkBoxSpecialPiece,
            minigameBtn,
            backToMainMenuBtn
        );

        errorText.maxWidth(boardPane.getWidth());
        errorPane.getChildren().addAll(errorText);

        setLeft(p1Pane);
        setRight(p2Pane);
        setCenter(boardPane);
        setBottom(hBoxBoardBtn);
        setTop(errorPane);
    }

    private void disableButtons() {
        comboBoxNrUndos.setDisable(jogoObservable.getUndoCreditsFromCurrentPlayer() == 0);
        saveGameBtn.setDisable(!jogoObservable.currentPlayerIsHuman());
        undoBtn.setDisable(jogoObservable.getUndoCreditsFromCurrentPlayer() == 0);
        minigameBtn.setDisable(!jogoObservable.currentPlayerCanPlayMinigames());
        checkBoxSpecialPiece.setDisable(!jogoObservable.currentPlayerHasSpecialPiece());
        backToMainMenuBtn.setDisable(!jogoObservable.currentPlayerIsHuman());
    }
}
