package pt.isec.angelopaiva.jogo.iu.gui.resources;

import javafx.scene.text.Font;

public class FontManager {
    private FontManager() {}

    public static Font loadFont(String name, double size) {
        return Font.loadFont(Resources.getResourceFileAsStream("fonts/" + name), size);
    }
}
