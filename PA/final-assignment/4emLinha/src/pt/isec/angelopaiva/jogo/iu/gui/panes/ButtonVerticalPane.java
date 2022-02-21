package pt.isec.angelopaiva.jogo.iu.gui.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;
import pt.isec.angelopaiva.jogo.iu.gui.resources.FontManager;
import pt.isec.angelopaiva.jogo.iu.gui.resources.fonts.ProjectFonts;

public class ButtonVerticalPane extends VBox {
    private final VBox vBoxBtn;
    private final Text title;

    public ButtonVerticalPane(String title) {
        setSpacing(20);

        vBoxBtn = new VBox(20);

        this.title = new Text(title);

        createView();
    }

    public void addNodesToButtonBox(Node... nodes) { vBoxBtn.getChildren().addAll(nodes); }

    public void addAllChildren(Node ... nodes) {
        int indexLastChild = getChildren().size() - 1;

        getChildren().remove(indexLastChild, indexLastChild + 1);
        getChildren().addAll(nodes);
        getChildren().add(vBoxBtn);
    }

    private void createView() {
        vBoxBtn.setAlignment(Pos.CENTER);
        vBoxBtn.setPadding(new Insets(25));
        vBoxBtn.setMinWidth(300);
        vBoxBtn.setBackground(new Background(new BackgroundFill(DraculaTheme.BACKGROUND_LIGHT, CornerRadii.EMPTY, Insets.EMPTY)));

        title.setFont(FontManager.loadFont(ProjectFonts.ABRIL_TEXT, 64));
        title.setFill(DraculaTheme.PINK);
        title.setTextAlignment(TextAlignment.CENTER);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);

        setEffect(dropShadow);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(DraculaTheme.BACKGROUND_LIGHT, CornerRadii.EMPTY, Insets.EMPTY)));
        
        getChildren().addAll(title, vBoxBtn);
    }
}
