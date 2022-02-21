package pt.isec.angelopaiva.jogo.iu.gui.panes.player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;
import pt.isec.angelopaiva.jogo.iu.gui.resources.FontManager;
import pt.isec.angelopaiva.jogo.iu.gui.resources.fonts.ProjectFonts;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;
import pt.isec.angelopaiva.jogo.logica.Property;

public abstract class PlayerPane extends VBox {
    protected final JogoObservable jogoObservable;

    protected final Text name;
    protected final Text info;

    protected PlayerPane(JogoObservable jogoObservable) {
        this.jogoObservable = jogoObservable;

        name = new Text();
        info = new Text();

        createView();
        registerObserver();
    }

    protected abstract void update(boolean isReplay);

    private void registerObserver() {
        jogoObservable.addPropertyChangeListener(Property.PROPERTY_GAME.toString(), evt -> update(false));
        jogoObservable.addPropertyChangeListener(Property.PROPERTY_MINIGAME.toString(), evt -> update(false));
        jogoObservable.addPropertyChangeListener(Property.PROPERTY_REPLAY.toString(), evt -> update(true));
    }

    private void createView() {
        setSpacing(20);

        setMinWidth(270);
        setBackground(new Background(new BackgroundFill(DraculaTheme.BACKGROUND_LIGHT, CornerRadii.EMPTY, Insets.EMPTY)));

        name.setFont(FontManager.loadFont(ProjectFonts.ABRIL_TEXT, 32));
        name.setFill(DraculaTheme.BACKGROUND);
        name.maxWidth(270);
        name.setWrappingWidth(270);
        name.setTextAlignment(TextAlignment.CENTER);

        info.setTextAlignment(TextAlignment.CENTER);
        info.setFill(DraculaTheme.FOREGROUND);

        setAlignment(Pos.CENTER);
        getChildren().addAll(name, info);
    }
}
