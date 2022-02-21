package pt.isec.forgotten.ex1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RootPane extends BorderPane {
    Stage stage;

    Button btnGreen, btnBlue;

    Label lblGreen, lblBlue;
    int nrGreenClicks, nrBlueClicks;

    public RootPane(Stage stage) {
        this.stage = stage;

        nrGreenClicks = 0;
        nrBlueClicks = 0;

        createLayout();
        registerListeners();
    }

    void changeBackground(Region region, Color color) {
        region.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    void updateLabels() {
        lblBlue.setText("#Blue: " + nrBlueClicks);
        lblGreen.setText("#Green: " + nrGreenClicks);
    }

    private void createLayout() {
        btnBlue = new Button("Blue");
        btnGreen = new Button("Green");

        btnBlue.setFont(new Font("Fira Code", 12));
        btnBlue.setTextFill(Color.web("0xf8f8f2"));
        changeBackground(btnBlue, Color.web("0x424450"));

        btnGreen.setFont(new Font("Fira Code", 12));
        btnGreen.setTextFill(Color.web("0xf8f8f2"));
        changeBackground(btnGreen, Color.web("0x424450"));

        HBox toolBar = new HBox();
        toolBar.getChildren().addAll(btnGreen, btnBlue);
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setPadding(new Insets(10));
        toolBar.setSpacing(10);
        changeBackground(toolBar, Color.TRANSPARENT);
        this.setTop(toolBar);

        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(5));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Pane pane = new Pane();
                pane.setPrefSize(50, 50);
                changeBackground(pane, Color.web("0xff79c6"));
                grid.add(pane, i, j);

                MyThread thread = new MyThread(pane);
                thread.start();
            }
        }

        grid.setAlignment(Pos.CENTER);
        this.setCenter(grid);

        lblBlue = new Label("#Blue");
        lblGreen = new Label("#Green");
        AnchorPane labels = new AnchorPane();

        labels.getChildren().addAll(lblBlue, lblGreen);

        lblBlue.setFont(new Font("Fira Code", 14));
        lblBlue.setTextFill(Color.web("0x6272a4"));
        AnchorPane.setLeftAnchor(lblBlue, 10.0);
        AnchorPane.setBottomAnchor(lblBlue, 10.0);

        lblGreen.setFont(new Font("Fira Code", 14));
        lblGreen.setTextFill(Color.web("0x6272a4"));
        AnchorPane.setRightAnchor(lblGreen, 10.0);
        AnchorPane.setBottomAnchor(lblGreen, 10.0);

        this.setBottom(labels);
    }

    private void registerListeners() {
        btnGreen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                nrGreenClicks++;
                changeBackground(RootPane.this, Color.web("0x50fa7b"));
                updateLabels();
            }
        });

        btnBlue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                nrBlueClicks++;
                changeBackground(RootPane.this, Color.web("0x8be9fd"));
                updateLabels();
            }
        });
    }

    class MyThread extends Thread {
        Region region;

        public MyThread(Region region) {
            this.region = region;
        }

        @Override
        public void run() {
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (true) {
                changeBackground(region, Color.color(Math.random(), Math.random(), Math.random()));
                try {
                    Thread.sleep((long) (Math.random() * 100));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("I'm still breathing " + getId() + "!");
            }
        }
    }
}
