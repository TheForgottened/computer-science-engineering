package pt.isec.angelopaiva.jogo.iu.gui.estados;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import pt.isec.angelopaiva.jogo.iu.gui.controls.CustomButton;
import pt.isec.angelopaiva.jogo.iu.gui.nodes.ErrorText;
import pt.isec.angelopaiva.jogo.iu.gui.panes.ButtonHorizontalPane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.ErrorPane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.player.P1Pane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.player.P2Pane;
import pt.isec.angelopaiva.jogo.iu.gui.panes.player.PlayerPane;
import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;
import pt.isec.angelopaiva.jogo.iu.gui.resources.FontManager;
import pt.isec.angelopaiva.jogo.iu.gui.resources.fonts.ProjectFonts;
import pt.isec.angelopaiva.jogo.logica.AppStates;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;
import pt.isec.angelopaiva.jogo.logica.Property;

public class MinigameForSpecialPiecePane extends BorderPane {
    private final JogoObservable jogoObservable;

    private final ErrorText errorText;

    private final Text wording;
    private final Text question;
    private final TextField textFieldAnswer;

    private final PlayerPane p1Pane;
    private final PlayerPane p2Pane;

    private final CustomButton submitBtn;

    public MinigameForSpecialPiecePane(JogoObservable jogoObservable) {
        this.jogoObservable = jogoObservable;

        errorText = new ErrorText();

        wording = new Text();
        question = new Text();
        textFieldAnswer = new TextField();

        p1Pane = new P1Pane(jogoObservable);
        p2Pane = new P2Pane(jogoObservable);

        submitBtn = new CustomButton("Submeter Resposta");

        createView();
        registerObserver();
        updateVisilibity();
    }

    private void update() {
        if (!isVisible()) return;

        wording.setText(jogoObservable.getMinigameWording() + ":");
        question.setText(jogoObservable.getMinigameQuestion());

        textFieldAnswer.clear();
        textFieldAnswer.requestFocus();
    }

    private void updateVisilibity() { setVisible(jogoObservable.getCurrentState() == AppStates.MINIGAME_FOR_SPECIAL_PIECE); }

    private void registerObserver() {
        jogoObservable.addPropertyChangeListener(Property.PROPERTY_MINIGAME.toString(), evt -> update());
        jogoObservable.addPropertyChangeListener(Property.PROPERTY_GENERAL.toString(), evt -> updateVisilibity());
    }

    private void createView() {
        ErrorPane errorPane = new ErrorPane();
        ButtonHorizontalPane hBoxBtn = new ButtonHorizontalPane();

        submitBtn.setOnAction(e -> jogoObservable.setMinigameAnswer(textFieldAnswer.getText()));

        hBoxBtn.getChildren().addAll(submitBtn);

        VBox vBoxMinigame = new VBox(30);

        wording.setFont(FontManager.loadFont(ProjectFonts.ABRIL_TEXT, 32));
        wording.setFill(DraculaTheme.PINK);
        wording.setWrappingWidth(600);
        wording.setTextAlignment(TextAlignment.CENTER);

        question.setFont(FontManager.loadFont(ProjectFonts.ABRIL_TEXT, 24));
        question.setFill(DraculaTheme.FOREGROUND);
        question.setWrappingWidth(600);
        question.setTextAlignment(TextAlignment.CENTER);

        textFieldAnswer.setMinHeight(50);
        textFieldAnswer.setMaxWidth(500);
        textFieldAnswer.setAlignment(Pos.CENTER);

        vBoxMinigame.setAlignment(Pos.CENTER);
        vBoxMinigame.getChildren().addAll(wording, question, textFieldAnswer);

        setOnKeyPressed(e -> {
            if (e.getCode() != KeyCode.ENTER) return;

            jogoObservable.setMinigameAnswer(textFieldAnswer.getText());
        });

        errorPane.getChildren().addAll(errorText);

        setLeft(p1Pane);
        setRight(p2Pane);
        setCenter(vBoxMinigame);
        setBottom(hBoxBtn);
        setTop(errorPane);
    }
}
