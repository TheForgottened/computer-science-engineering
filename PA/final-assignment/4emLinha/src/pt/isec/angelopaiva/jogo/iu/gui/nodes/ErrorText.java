package pt.isec.angelopaiva.jogo.iu.gui.nodes;

import javafx.scene.text.TextAlignment;
import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;

public class ErrorText extends javafx.scene.text.Text {
    public ErrorText() {
        setFill(DraculaTheme.RED);
        setTextAlignment(TextAlignment.CENTER);
    }
}
