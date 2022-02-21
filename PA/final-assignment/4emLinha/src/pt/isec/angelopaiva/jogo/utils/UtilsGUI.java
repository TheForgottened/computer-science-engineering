package pt.isec.angelopaiva.jogo.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;

import java.util.concurrent.ThreadLocalRandom;

public class UtilsGUI {
    private UtilsGUI() {}

    public static VBox getBackgroundVBox() {
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setColor(Color.BLACK);

        VBox vBoxReturn = new VBox();

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(15));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                StackPane stackPane = new StackPane();
                Circle circle = new Circle();

                switch (ThreadLocalRandom.current().nextInt(2)) {
                    case 0 -> circle.setFill(DraculaTheme.RED);
                    case 1 -> circle.setFill(DraculaTheme.PURPLE);
                    default -> circle.setFill(DraculaTheme.BACKGROUND);
                }

                circle.setRadius(30);
                circle.setEffect(innerShadow);

                stackPane.getChildren().addAll(circle);
                gridPane.add(stackPane, i, j);
            }
        }

        gridPane.setAlignment(Pos.CENTER_LEFT);

        vBoxReturn.setScaleX(4);
        vBoxReturn.setScaleY(4);
        vBoxReturn.getTransforms().add(new Rotate(-30, 90, 90));
        vBoxReturn.setBackground(new Background(new BackgroundFill(DraculaTheme.BACKGROUND_LIGHTER, CornerRadii.EMPTY, Insets.EMPTY)));
        vBoxReturn.getChildren().addAll(gridPane);
        vBoxReturn.setAlignment(Pos.CENTER_RIGHT);

        return vBoxReturn;
    }
}
