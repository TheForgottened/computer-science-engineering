package pt.isec.angelopaiva.jogo.iu.gui.resources;

import javafx.scene.Parent;

public class CSSManager {
    private CSSManager() {}

    public static void setCSS(Parent parent, String fileName) {
        parent.getStylesheets().add(Resources.getResourceFilename("styles/" + fileName));
    }
}
