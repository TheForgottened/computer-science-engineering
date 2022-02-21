package pt.isec.angelopaiva.jogo.iu.gui.estados;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import pt.isec.angelopaiva.jogo.iu.gui.controls.CustomButton;
import pt.isec.angelopaiva.jogo.iu.gui.nodes.ErrorText;
import pt.isec.angelopaiva.jogo.iu.gui.panes.BoardPane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.ButtonHorizontalPane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.ErrorPane;
import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;
import pt.isec.angelopaiva.jogo.iu.gui.resources.FontManager;
import pt.isec.angelopaiva.jogo.iu.gui.resources.ImageLoader;
import pt.isec.angelopaiva.jogo.iu.gui.resources.SoundPlayer;
import pt.isec.angelopaiva.jogo.iu.gui.resources.fonts.ProjectFonts;
import pt.isec.angelopaiva.jogo.iu.gui.resources.images.ProjectImages;
import pt.isec.angelopaiva.jogo.iu.gui.resources.sounds.ProjectSounds;
import pt.isec.angelopaiva.jogo.logica.AppStates;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;
import pt.isec.angelopaiva.jogo.logica.Property;

public class EndGamePane extends BorderPane {
    private final JogoObservable jogoObservable;

    private final ErrorText errorText;

    private final CustomButton backToMainMenuBtn;

    private final VBox vBoxP1;
    private final Text nameP1;
    private final ImageView imageP1;

    private final VBox vBoxP2;
    private final Text nameP2;
    private final ImageView imageP2;

    private final BoardPane boardPane;

    public EndGamePane(JogoObservable jogoObservable) {
        this.jogoObservable = jogoObservable;

        errorText = new ErrorText();

        backToMainMenuBtn = new CustomButton("Menu Inicial");

        vBoxP1 = new VBox(20);
        nameP1 = new Text();
        imageP1 = new ImageView();

        vBoxP2 = new VBox(20);
        nameP2 = new Text();
        imageP2 = new ImageView();

        boardPane = new BoardPane(jogoObservable);

        createView();
        registerObserver();
        update();
    }

    private void update() {
        if (jogoObservable.getCurrentState() != AppStates.END_GAME) {
            setVisible(false);
            return;
        }

        setVisible(true);

        errorText.setText("");

        nameP1.setText(jogoObservable.getP1Name());
        nameP2.setText(jogoObservable.getP2Name());

        switch (jogoObservable.getJogoState()) {
            case VICTORY_P1 -> {
                imageP1.opacityProperty().set(1);
                imageP2.opacityProperty().set(0);
            }

            case VICTORY_P2 -> {
                imageP1.opacityProperty().set(0);
                imageP2.opacityProperty().set(1);
            }

            case DRAW -> {
                imageP1.opacityProperty().set(1);
                imageP2.opacityProperty().set(1);
            }

            default -> {
                imageP1.opacityProperty().set(0);
                imageP2.opacityProperty().set(0);
            }
        }

        SoundPlayer.playSound(ProjectSounds.END_GAME);
    }

    private void registerObserver() { jogoObservable.addPropertyChangeListener(Property.PROPERTY_GENERAL.toString(), evt -> update()); }

    private void createView() {
        ErrorPane errorPane = new ErrorPane();
        ButtonHorizontalPane hBoxBoardBtn = new ButtonHorizontalPane();

        backToMainMenuBtn.setOnAction(e -> jogoObservable.goBack());

        hBoxBoardBtn.getChildren().addAll(backToMainMenuBtn);

        errorText.maxWidth(boardPane.getWidth());

        errorPane.getChildren().addAll(errorText);

        createViewVBoxPlayer(vBoxP1, nameP1, imageP1);
        createViewVBoxPlayer(vBoxP2, nameP2, imageP2);

        setLeft(vBoxP1);
        setRight(vBoxP2);
        setCenter(boardPane);
        setBottom(hBoxBoardBtn);
        setTop(errorPane);
    }

    private void createViewVBoxPlayer(VBox vBoxPlayer, Text namePlayer, ImageView imagePlayer) {
        final int FONT_SIZE = 32;
        final int PREF_WIDTH = 270;
        final int PREF_IMAGE_SIZE = 60;

        vBoxPlayer.setMinWidth(PREF_WIDTH);
        vBoxPlayer.setBackground(new Background(new BackgroundFill(DraculaTheme.BACKGROUND_LIGHT, CornerRadii.EMPTY, Insets.EMPTY)));

        namePlayer.setFont(FontManager.loadFont(ProjectFonts.ABRIL_TEXT, FONT_SIZE));
        namePlayer.setFill(DraculaTheme.YELLOW);
        namePlayer.maxWidth(PREF_WIDTH);
        namePlayer.setWrappingWidth(PREF_WIDTH);
        namePlayer.setTextAlignment(TextAlignment.CENTER);

        imagePlayer.setImage(ImageLoader.getImage(ProjectImages.TROPHY));
        imagePlayer.setFitHeight(PREF_IMAGE_SIZE);
        imagePlayer.setFitWidth(PREF_IMAGE_SIZE);

        vBoxPlayer.setAlignment(Pos.CENTER);
        vBoxPlayer.getChildren().addAll(namePlayer, imagePlayer);
    }
}
