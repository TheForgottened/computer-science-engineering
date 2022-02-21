package pt.isec.angelopaiva.jogo.iu.gui.estados;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import pt.isec.angelopaiva.jogo.iu.gui.controls.CustomButton;
import pt.isec.angelopaiva.jogo.iu.gui.panes.ButtonVerticalPane;
import pt.isec.angelopaiva.jogo.logica.AppStates;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;
import pt.isec.angelopaiva.jogo.logica.Property;
import pt.isec.angelopaiva.jogo.utils.UtilsFiles;
import pt.isec.angelopaiva.jogo.utils.UtilsGUI;

import java.io.File;

public class ChooseOptionPane extends BorderPane {
    private final JogoObservable jogoObservable;

    private final CustomButton playGameBtn;
    private final CustomButton loadGameBtn;
    private final CustomButton watchReplayBtn;
    private final CustomButton exitBtn;

    public ChooseOptionPane(JogoObservable jogoObservable) {
        this.jogoObservable = jogoObservable;

        playGameBtn = new CustomButton("Novo Jogo");
        loadGameBtn = new CustomButton("Carregar Jogo");
        watchReplayBtn = new CustomButton("Ver Replay Jogo");
        exitBtn = new CustomButton("Sair");

        createView();
        registerObserver();
        update();
    }

    private void update() { setVisible(jogoObservable.getCurrentState() == AppStates.CHOOSE_OPTION); }

    private void registerObserver() { jogoObservable.addPropertyChangeListener(Property.PROPERTY_GENERAL.toString(), evt -> update()); }

    private void createView() {
        ButtonVerticalPane vBoxBtn = new ButtonVerticalPane("QUATRO\nEM\nLINHA");

        playGameBtn.setOnAction(e -> jogoObservable.startWaitPlayersNameAndType());

        loadGameBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(UtilsFiles.SAVES_PATH));
            fileChooser.setTitle("Abra um Save");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Ficheiros Save", "*.save"));

            File tempFile = fileChooser.showOpenDialog(this.getScene().getWindow());

            jogoObservable.loadGame(tempFile);
        });

        watchReplayBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(UtilsFiles.REPLAY_PATH));
            fileChooser.setTitle("Abra uma Replay");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Ficheiros Replay", "*.rsave"));

            File tempFile = fileChooser.showOpenDialog(getScene().getWindow());

            jogoObservable.loadGameForReplay(tempFile);
        });

        exitBtn.setOnAction(e -> fireEvent(new WindowEvent(getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST)));

        vBoxBtn.addNodesToButtonBox(playGameBtn, loadGameBtn, watchReplayBtn, exitBtn);
        vBoxBtn.setSpacing(150);

        VBox vBoxSide = UtilsGUI.getBackgroundVBox();

        setRight(vBoxSide);
        setLeft(vBoxBtn);
    }
}
