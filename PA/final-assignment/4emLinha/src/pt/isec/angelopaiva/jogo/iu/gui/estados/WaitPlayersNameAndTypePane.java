package pt.isec.angelopaiva.jogo.iu.gui.estados;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pt.isec.angelopaiva.jogo.iu.gui.controls.CustomButton;
import pt.isec.angelopaiva.jogo.iu.gui.controls.CustomRadioButton;
import pt.isec.angelopaiva.jogo.iu.gui.panes.ButtonVerticalPane;
import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;
import pt.isec.angelopaiva.jogo.logica.AppStates;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;
import pt.isec.angelopaiva.jogo.logica.Property;
import pt.isec.angelopaiva.jogo.logica.dados.players.Player;
import pt.isec.angelopaiva.jogo.logica.dados.players.PlayerAI;
import pt.isec.angelopaiva.jogo.logica.dados.players.PlayerHuman;
import pt.isec.angelopaiva.jogo.utils.UtilsGUI;

public class WaitPlayersNameAndTypePane extends BorderPane {
    private final JogoObservable jogoObservable;

    private final Text p1Text;
    private final TextField p1TextField;

    private final CustomRadioButton p1AiRadio;
    private final CustomRadioButton p1HumanRadio;
    private final ToggleGroup p1RadioGroup;

    private final Text p2Text;
    private final TextField p2TextField;

    private final CustomRadioButton p2AiRadio;
    private final CustomRadioButton p2HumanRadio;
    private final ToggleGroup p2RadioGroup;

    private final CustomButton startGameBtn;
    private final CustomButton goBackBtn;

    public WaitPlayersNameAndTypePane(JogoObservable jogoObservable) {
        this.jogoObservable = jogoObservable;

        p1Text = new Text("Informações do Jogador 1:");
        p1TextField = new TextField("Jogador1");

        p1AiRadio = new CustomRadioButton("IA");
        p1HumanRadio = new CustomRadioButton("Humano");
        p1RadioGroup = new ToggleGroup();

        p2Text = new Text("Informações do Jogador 2:");
        p2TextField = new TextField("Jogador2");

        p2AiRadio = new CustomRadioButton("IA");
        p2HumanRadio = new CustomRadioButton("Humano");
        p2RadioGroup = new ToggleGroup();

        startGameBtn = new CustomButton("Começar Jogo");
        goBackBtn = new CustomButton("Voltar");

        createView();
        registerObserver();
        update();
    }

    private void update() {
        setVisible(jogoObservable.getCurrentState() == AppStates.WAIT_PLAYERS_NAME_AND_TYPE);

        if (!isVisible()) return;

        p1TextField.setText("Jogador1");
        p1AiRadio.setSelected(true);

        p2TextField.setText("Jogador2");
        p2AiRadio.setSelected(true);
    }

    private void registerObserver() { jogoObservable.addPropertyChangeListener(Property.PROPERTY_GENERAL.toString(), evt -> update()); }

    private void createView() {
        ButtonVerticalPane vBoxBtn = new ButtonVerticalPane("QUATRO\nEM\nLINHA");

        VBox vBoxPlayerInfo = new VBox(20);

        HBox hBoxRadioP1 = createPlayerRadioBox(p1AiRadio, p1HumanRadio, p1RadioGroup);
        HBox hBoxRadioP2 = createPlayerRadioBox(p2AiRadio, p2HumanRadio, p2RadioGroup);

        p1Text.setFill(DraculaTheme.FOREGROUND);
        p1TextField.setMaxWidth(210);

        p2Text.setFill(DraculaTheme.FOREGROUND);
        p2TextField.setMaxWidth(210);

        vBoxPlayerInfo.setMaxWidth(300);
        vBoxPlayerInfo.setAlignment(Pos.CENTER);
        vBoxPlayerInfo.getChildren().addAll(p1Text, p1TextField, hBoxRadioP1, p2Text, p2TextField, hBoxRadioP2);

        startGameBtn.setOnAction(e -> {
            Player p1;
            Player p2;

            switch (p1RadioGroup.getToggles().indexOf(p1RadioGroup.getSelectedToggle())) {
                case 0 -> p1 = new PlayerAI(p1TextField.getText());
                case 1 -> p1 = new PlayerHuman(p1TextField.getText());
                default -> p1 = null;
            }

            switch (p2RadioGroup.getToggles().indexOf(p2RadioGroup.getSelectedToggle())) {
                case 0 -> p2 = new PlayerAI(p2TextField.getText());
                case 1 -> p2 = new PlayerHuman(p2TextField.getText());
                default -> p2 = null;
            }

            jogoObservable.setPlayers(p1, p2);
        });

        goBackBtn.setOnAction(e -> jogoObservable.goBack());

        vBoxBtn.setSpacing(29);
        vBoxBtn.addNodesToButtonBox(startGameBtn, goBackBtn);
        vBoxBtn.addAllChildren(vBoxPlayerInfo);

        VBox vBoxSide = UtilsGUI.getBackgroundVBox();

        setRight(vBoxSide);
        setLeft(vBoxBtn);
    }

    private HBox createPlayerRadioBox(CustomRadioButton playerAiRadio, CustomRadioButton playerHumanRadio, ToggleGroup playerRadioGroup) {
        HBox hBoxRadio = new HBox(20);

        playerAiRadio.setSelected(true);

        hBoxRadio.setAlignment(Pos.CENTER);
        hBoxRadio.getChildren().addAll(playerAiRadio, playerHumanRadio);

        playerAiRadio.setToggleGroup(playerRadioGroup);
        playerHumanRadio.setToggleGroup(playerRadioGroup);

        return hBoxRadio;
    }
}
