package pt.isec.forgotten.jfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class RootPane extends BorderPane {
    HBox toolBar, statusBar;
    Button btnNew, btnOpen, btnClose;
    Label msgStatus;

    public RootPane() {
        createLayout();
        registerListeners();
    }

    void createLayout() {
        toolBar = new HBox();
        statusBar = new HBox();
        btnNew = new Button("New");
        btnOpen = new Button("Open");
        btnClose = new Button("Close");

        toolBar.getChildren().addAll(btnNew, btnOpen, btnClose);
        msgStatus = new Label("Mensagem");
        statusBar.getChildren().addAll(msgStatus);
        this.setBottom(statusBar);
    }

    void registerListeners() {
        btnNew.setOnAction(new ProcBotao());
    }

    class ProcBotao implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            msgStatus.setText("very click");
        }
    }
}
