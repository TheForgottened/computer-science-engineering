package pt.isec.angelopaiva.jogo.iu.gui.panes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import pt.isec.angelopaiva.jogo.iu.gui.nodes.ErrorText;
import pt.isec.angelopaiva.jogo.iu.gui.resources.DraculaTheme;
import pt.isec.angelopaiva.jogo.iu.gui.resources.SoundPlayer;
import pt.isec.angelopaiva.jogo.iu.gui.resources.sounds.ProjectSounds;
import pt.isec.angelopaiva.jogo.logica.JogoObservable;
import pt.isec.angelopaiva.jogo.logica.Property;
import pt.isec.angelopaiva.jogo.logica.TypePiece;

public class BoardPane extends HBox {
    private static final int RADIUS_DIVIDEND_COLUMN = 300;
    private static final int RADIUS_DIVIDEND_ROW = 250;

    private final JogoObservable jogoObservable;

    public final GridPane gridPane;
    private final Shape[][] gridPaneShape;

    private final InnerShadow innerShadow;

    public BoardPane(JogoObservable jogoObservable) {
        this.jogoObservable = jogoObservable;

        gridPane = new GridPane();
        gridPaneShape = new Shape[jogoObservable.getNumberBoardRows()][jogoObservable.getNumberBoardColumns()];

        innerShadow = new InnerShadow();

        createView();
        registerObserver();
        update();
    }

    public BoardPane(JogoObservable jogoObservable, CheckBox checkBoxSpecialPiece) {
        this.jogoObservable = jogoObservable;

        gridPane = new GridPane();
        gridPaneShape = new Shape[jogoObservable.getNumberBoardRows()][jogoObservable.getNumberBoardColumns()];

        innerShadow = new InnerShadow();

        createView();
        registerObserver();
        update();

        setPieceOnMouseEventListeners(checkBoxSpecialPiece);
    }

    public BoardPane(JogoObservable jogoObservable, CheckBox checkBoxSpecialPiece, ErrorText errorText) {
        this.jogoObservable = jogoObservable;

        gridPane = new GridPane();
        gridPaneShape = new Shape[jogoObservable.getNumberBoardRows()][jogoObservable.getNumberBoardColumns()];

        innerShadow = new InnerShadow();

        createView();
        registerObserver();
        update();

        setPieceOnMouseEventListeners(checkBoxSpecialPiece, errorText);
    }

    private void update() {
        TypePiece[][] board = jogoObservable.getCopyOfBoard();

        if (board.length == 0) return;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                switch (board[i][j]) {
                    case PIECE_P1 -> gridPaneShape[i][j].setFill(DraculaTheme.RED);
                    case PIECE_P2 -> gridPaneShape[i][j].setFill(DraculaTheme.PURPLE);
                    case NONE -> gridPaneShape[i][j].setFill(DraculaTheme.BACKGROUND);
                }
            }
        }
    }

    private void registerObserver() {
        jogoObservable.addPropertyChangeListener(Property.PROPERTY_GAME.toString(), evt -> update());
        jogoObservable.addPropertyChangeListener(Property.PROPERTY_REPLAY.toString(), evt -> update());
    }

    private void createView() {
        createGridPaneView();

        setSpacing(20);
        setMaxWidth(700);
        setMaxHeight(600);
        setAlignment(Pos.CENTER);
        setBackground(new Background(new BackgroundFill(DraculaTheme.BACKGROUND_LIGHTER.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));
        getChildren().add(gridPane);
    }

    private void createGridPaneView() {
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(15));

        innerShadow.setColor(Color.BLACK);

        for (int i = 0; i < jogoObservable.getNumberBoardRows(); i++) {
            for (int j = 0; j < jogoObservable.getNumberBoardColumns(); j++) {
                StackPane stackPane = new StackPane();
                Circle circle = new Circle();

                circle.setFill(DraculaTheme.BACKGROUND);

                double radius = Math.min(
                        (double) RADIUS_DIVIDEND_COLUMN / jogoObservable.getNumberBoardColumns(),
                        (double) RADIUS_DIVIDEND_ROW / jogoObservable.getNumberBoardRows()
                );

                circle.setRadius(radius);
                circle.setEffect(innerShadow);

                stackPane.setBackground(new Background(new BackgroundFill(DraculaTheme.BACKGROUND_LIGHTER.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));
                stackPane.getChildren().addAll(circle);

                gridPane.add(stackPane, j, i);
                gridPaneShape[i][j] = circle;
            }
        }

        gridPane.setAlignment(Pos.CENTER);

        setAlignment(Pos.CENTER);
    }

    public void setPieceOnMouseEventListeners(CheckBox checkBoxSpecialPiece) {
        for (Shape[] shapesRow : gridPaneShape) {
            for (int i = 0; i < shapesRow.length; i++) {
                int finalI = i;

                shapesRow[i].setOnMouseClicked(e -> {
                    if (e.getButton() != MouseButton.PRIMARY) return;

                    if (checkBoxSpecialPiece.isSelected()) {
                        jogoObservable.placePieceOnColumn(finalI, true);
                        SoundPlayer.playSound(ProjectSounds.SPECIAL_PIECE);
                        return;
                    }

                    if (!jogoObservable.placePieceOnColumn(finalI, false)) { return; }

                    SoundPlayer.playSound(ProjectSounds.PIECE_FALLING);
                });

                setOnMouseHover(checkBoxSpecialPiece, finalI, shapesRow[i]);
            }
        }
    }

    public void setPieceOnMouseEventListeners(CheckBox checkBoxSpecialPiece, ErrorText errorText) {
        for (Shape[] shapesRow : gridPaneShape) {
            for (int i = 0; i < shapesRow.length; i++) {
                int finalI = i;

                shapesRow[i].setOnMouseClicked(e -> {
                    if (e.getButton() != MouseButton.PRIMARY) return;

                    if (checkBoxSpecialPiece.isSelected()) {
                        jogoObservable.placePieceOnColumn(finalI, true);
                        SoundPlayer.playSound(ProjectSounds.SPECIAL_PIECE);
                        return;
                    }

                    if (!jogoObservable.placePieceOnColumn(finalI, false)) {
                        errorText.setText("Não pode jogar nessa coluna!");
                        return;
                    }

                    SoundPlayer.playSound(ProjectSounds.PIECE_FALLING);
                });

                setOnMouseHover(checkBoxSpecialPiece, finalI, shapesRow[i]);
            }
        }
    }

    private void setOnMouseHover(CheckBox checkBoxSpecialPiece, int finalI, Shape shape) {
        shape.setOnMouseEntered(e -> {
            TypePiece[][] board = jogoObservable.getCopyOfBoard();

            if (checkBoxSpecialPiece.isSelected()) {
                for (Shape[] shapes : gridPaneShape) {
                    shapes[finalI].setFill(Color.color(
                            DraculaTheme.FOREGROUND.getRed(),
                            DraculaTheme.FOREGROUND.getGreen(),
                            DraculaTheme.FOREGROUND.getBlue(),
                            0.50
                    ));
                }
                return;
            }

            if (board[0][finalI] != TypePiece.NONE) return;

            for (int x = 0; x < gridPaneShape.length; x++) {
                if (x + 1 == board.length || board[x + 1][finalI] != TypePiece.NONE) {
                    switch (jogoObservable.getCurrentPlayer()) {
                        case 1 -> gridPaneShape[x][finalI].setFill(Color.color(
                                DraculaTheme.RED.getRed(),
                                DraculaTheme.RED.getGreen(),
                                DraculaTheme.RED.getBlue(),
                                0.50
                        ));
                        case 2 -> gridPaneShape[x][finalI].setFill(Color.color(
                                DraculaTheme.PURPLE.getRed(),
                                DraculaTheme.PURPLE.getGreen(),
                                DraculaTheme.PURPLE.getBlue(),
                                0.50
                        ));
                        default -> gridPaneShape[x][finalI].setFill(DraculaTheme.BACKGROUND);
                    }
                    break;
                }
            }
        });

        shape.setOnMouseExited(e -> {
            TypePiece[][] board = jogoObservable.getCopyOfBoard();

            for (int x = 0; x < board.length; x++) {
                switch (board[x][finalI]) {
                    case PIECE_P1 -> gridPaneShape[x][finalI].setFill(DraculaTheme.RED);
                    case PIECE_P2 -> gridPaneShape[x][finalI].setFill(DraculaTheme.PURPLE);
                    case NONE -> gridPaneShape[x][finalI].setFill(DraculaTheme.BACKGROUND);
                }
            }
        });
    }
}
