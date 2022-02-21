package pt.isec.angelopaiva.jogo.iu.gui.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;

public class ButtonHorizontalPane extends HBox {
    public ButtonHorizontalPane() {
        setSpacing(20);
        setMinHeight(50);
        setMinWidth(50);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setBackground(new Background(new BackgroundFill(DraculaTheme.BACKGROUND_LIGHT, CornerRadii.EMPTY, Insets.EMPTY)));
    }
}
