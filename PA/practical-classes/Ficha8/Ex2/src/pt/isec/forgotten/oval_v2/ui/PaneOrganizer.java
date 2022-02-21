package pt.isec.forgotten.oval_v2.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import pt.isec.forgotten.oval_v2.logica.Desenho;

public class PaneOrganizer extends BorderPane {
    private static final int TAM_BTN = 40;

    Desenho desenho;
    AreaDesenho areaDesenho;
    HBox toolbar;
    Button btnClearLast, btnClearAll;
    Button btnRed, btnGreen, btnBlue, btnRandomColor;

    public PaneOrganizer(Desenho desenho) {
        this.desenho = desenho;
        criarLayout();
        registarListeners();
    }

    void criarLayout() {
        toolbar = new HBox();

        btnClearLast = new Button("Apaga Último");
        btnClearLast.setPrefHeight(TAM_BTN);

        btnClearAll = new Button("Apaga Tudo");
        btnClearAll.setPrefHeight(TAM_BTN);

        btnRed = new Button();
        btnRed.setPrefSize(TAM_BTN, TAM_BTN);;
        btnRed.setBackground(new Background(new BackgroundFill(Color.web("0xff5555"), CornerRadii.EMPTY, Insets.EMPTY)));

        btnGreen = new Button();
        btnGreen.setPrefSize(TAM_BTN, TAM_BTN);
        btnGreen.setBackground(new Background(new BackgroundFill(Color.web("0x50fa7b"), CornerRadii.EMPTY, Insets.EMPTY)));

        btnBlue = new Button();
        btnBlue.setPrefSize(TAM_BTN, TAM_BTN);
        btnBlue.setBackground(new Background(new BackgroundFill(Color.web("0x8be9fd"), CornerRadii.EMPTY, Insets.EMPTY)));

        btnRandomColor = new Button("?");
        btnRandomColor.setPrefSize(TAM_BTN, TAM_BTN);
        MyThread thread = new MyThread(btnRandomColor);
        thread.setDaemon(true);
        thread.start();

        toolbar.getChildren().addAll(btnClearLast, new Line(0,0,0,30), btnRed, btnGreen, btnBlue, btnRandomColor, new Line(0,0,0,30), btnClearAll);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setSpacing(20);
        toolbar.setPadding(new Insets(10));
        toolbar.setBackground(new Background(new BackgroundFill(Color.web("0x424450"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.setTop(toolbar);

        areaDesenho = new AreaDesenho(desenho);
        this.setCenter(areaDesenho);
    }

    ChangeListener procSize = new ChangeListener() {
        @Override
        public void changed(ObservableValue observableValue, Object o, Object t1) {
            areaDesenho.alteraDims(PaneOrganizer.this.getWidth(), PaneOrganizer.this.getHeight());
        }
    };

    void registarListeners() {
        btnClearLast.setOnAction(e -> {
            desenho.apagaUltima();
            areaDesenho.atualiza();
        });

        btnClearAll.setOnAction(e-> {
            desenho.apagaTudo();
            areaDesenho.atualiza();
        });

        btnRed.setOnAction(e-> {
            desenho.setRGB(Color.web("0xff5555").getRed(), Color.web("0xff5555").getGreen(), Color.web("0xff5555").getBlue());
            atualizaBotaoAtivo();
        });

        btnGreen.setOnAction(e-> {
            desenho.setRGB(Color.web("0x50fa7b").getRed(), Color.web("0x50fa7b").getGreen(), Color.web("0x50fa7b").getBlue());
            atualizaBotaoAtivo();
        });

        btnBlue.setOnAction(e-> {
            desenho.setRGB(Color.web("0x8be9fd").getRed(), Color.web("0x8be9fd").getGreen(), Color.web("0x8be9fd").getBlue());
            atualizaBotaoAtivo();
        });

        btnRandomColor.setOnAction(e-> {
            desenho.setRGB(Math.random(),Math.random(),Math.random());
            atualizaBotaoAtivo();
        });

        widthProperty().addListener(procSize);
        heightProperty().addListener(procSize);
    }

    void atualizaBotaoAtivo() {
        Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));

        double r = desenho.getR();
        double g = desenho.getG();
        double b = desenho.getB();
        btnRed.setBorder(r > 0 && g==0 && b==0 ? border : null);
        btnGreen.setBorder(g > 0 && r==0 && b==0 ? border : null);
        btnBlue.setBorder(b > 0 && r==0 && g==0 ? border : null);
        btnRandomColor.setBorder(r*g!=0 || r*b!=0 || g*b!=0 ? border : null);
    }

    void clear(GraphicsContext gc) {
        gc.setFill(Color.rgb(255,255,255));
        gc.fillRect(0,0,areaDesenho.getWidth(),areaDesenho.getHeight());
    }

    class MyThread extends Thread {
        Region region;

        public MyThread(Region region) {
            this.region = region;
        }

        @Override
        public void run() {
            while (true) {
                region.setBackground(new Background(new BackgroundFill(Color.color(Math.random(), Math.random(), Math.random()), CornerRadii.EMPTY, Insets.EMPTY)));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("I'm still breathing " + getId() + "!");
            }
        }
    }
}

