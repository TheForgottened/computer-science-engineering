package pt.isec.forgotten.ex1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        RootPane root = new RootPane(stage);
        Scene scene = new Scene(root, 800, 600, Color.web("0x424450"));
        stage.setScene(scene);
        stage.setMinHeight(200);
        stage.setMinWidth(200);
        stage.setTitle("Exercício 1");

        stage.show();
    }
}

