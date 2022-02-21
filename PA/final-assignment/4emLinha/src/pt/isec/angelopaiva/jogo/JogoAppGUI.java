package pt.isec.angelopaiva.jogo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.angelopaiva.jogo.iu.gui.PaneOrganizer;
import pt.isec.angelopaiva.jogo.iu.gui.resources.ImageLoader;
import pt.isec.angelopaiva.jogo.iu.gui.resources.images.ProjectImages;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;
import pt.isec.angelopaiva.jogo.logica.StateMachine;
import pt.isec.angelopaiva.jogo.utils.UtilsFiles;

public class JogoAppGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StateMachine sm = new StateMachine();
        JogoObservable jogoObservable = new JogoObservable(sm);

        PaneOrganizer root = new PaneOrganizer(jogoObservable);

        Scene scene = new Scene(root, 1280, 720); // 16:9 HD Standard

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quatro em Linha");
        primaryStage.getIcons().add(ImageLoader.getImage(ProjectImages.ICON));

        primaryStage.setOnCloseRequest(windowEvent -> Platform.exit());

        primaryStage.show();

        /*
        Stage primaryStage2 = new Stage();
        PaneOrganizer root2 = new PaneOrganizer(jogoObservable);
        Scene scene2 = new Scene(root2, 1280, 720); // 16:9 HD Standard
        primaryStage2.setResizable(false);
        primaryStage2.setScene(scene2);
        primaryStage2.setTitle("Quatro em Linha");
        primaryStage2.setOnCloseRequest(windowEvent -> Platform.exit());
        primaryStage2.getIcons().add(ImageLoader.getImage(ProjectImages.ICON));
        primaryStage2.show();
        */
    }

    public static void main(String[] args) {
        UtilsFiles.prepareDirectories();
        launch(args);
    }
}
