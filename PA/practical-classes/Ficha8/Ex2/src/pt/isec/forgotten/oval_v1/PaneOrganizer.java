package pt.isec.forgotten.oval_v1;

import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import javafx.scene.input.MouseEvent;
import pt.isec.forgotten.oval_v1.logica.Figura;

class PaneOrganizer extends Pane {
    Figura figura;
    Ellipse ellipse;

    public PaneOrganizer() {
        criarLayout();
        registarListeners();
    }

    void criarLayout() {
        figura = new Figura();
        ellipse = new Ellipse(100, 100, 80, 50);
        this.getChildren().add(ellipse);
    }

    void registarListeners() {
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            if (figura == null) return;

            figura.setRGB(Math.random(), Math.random(), Math.random());
            figura.setP1(e.getX(), e.getY());
            figura.setP2(e.getX(), e.getY());

            atualiza();
        });

        this.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            figura.setP2(e.getX(), e.getY());
            atualiza();
        });
    }

    void atualiza() {
        if (ellipse == null || figura == null) return;

        ellipse.setCenterX(figura.getCX());
        ellipse.setCenterY(figura.getCY());
        ellipse.setRadiusX(figura.getLargura() / 2);
        ellipse.setRadiusY(figura.getAltura() / 2);
        ellipse.setFill(Color.color(figura.getR(), figura.getG(), figura.getB()));
    }
}
