package pt.isec.forgotten.oval_v1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        PaneOrganizer root = new PaneOrganizer();
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Exercício 2");
        stage.setMinWidth(300);
        stage.setMinHeight(300);

        stage.show();
    }
}

