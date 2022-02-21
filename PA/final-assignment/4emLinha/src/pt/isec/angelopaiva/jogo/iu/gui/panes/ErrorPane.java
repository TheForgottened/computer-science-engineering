package pt.isec.angelopaiva.jogo.iu.gui.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;

public class ErrorPane extends HBox {
    public ErrorPane() {
        setSpacing(20);
        setPadding(new Insets(2));
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(DraculaTheme.BACKGROUND_LIGHT, CornerRadii.EMPTY, Insets.EMPTY)));
    }
}
