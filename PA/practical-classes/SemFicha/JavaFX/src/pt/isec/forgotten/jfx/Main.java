package pt.isec.forgotten.jfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        System.out.println("Estou aqui!");
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("init");
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("start");

        RootPane root = new RootPane();
        Button b1 = new Button("Button 1");
        Button b2 = new Button("Button 2");
        root.getChildren().addAll(b1, b2);

        Scene scene = new Scene(root, 800, 600, Color.web("0x282A36", 1));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("much application");
        stage.getIcons().add(new Image("/resources/icons/dogecoin.png"));

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("stop");
        Platform.exit();
    }
}
