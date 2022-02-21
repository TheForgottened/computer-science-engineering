package pt.isec.angelopaiva.jogo.iu.gui;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;

public class PaneOrganizer extends BorderPane {
    private final PrincipalPane principalPane;

    public PaneOrganizer(JogoObservable jogoObservable) {
        principalPane = new PrincipalPane(jogoObservable);

        createLayout();
    }

    private void createLayout() {
        principalPane.setBackground(new Background(new BackgroundFill(DraculaTheme.BACKGROUND, CornerRadii.EMPTY, Insets.EMPTY)));

        setCenter(principalPane);
    }
}
